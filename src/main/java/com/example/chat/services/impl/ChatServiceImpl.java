package com.example.chat.services.impl;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.enums.Availability;
import com.example.chat.dto.enums.StateMessage;
import com.example.chat.dto.enums.TypeParticipant;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.ParticipantEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.mappers.ChatMapper;
import com.example.chat.repositories.ChatRepository;
import com.example.chat.repositories.MessageRepository;
import com.example.chat.repositories.ParticipantRepository;
import com.example.chat.repositories.UserRepository;
import com.example.chat.services.ChatService;
import com.example.chat.services.ParticipantService;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ParticipantRepository participantRepository;
    private final ChatMapper chatMapper;

    /**
     * Если чат PRIVATE и он уже есть, то возвращается он, а если нет, то создаётся новый
     */
    @Override
    @Transactional
    public ChatDto createChat(Long userId, ChatDto chat) {
        Set<Long> participantsIds = chat.getParticipants().size() > 0
                ? chat.getParticipants()
                : Set.of();

        //проверяем существуют ли такие пользователи по списку ИД
        userRepository.findAllByIdIn(participantsIds);
        chat.setParticipants(participantsIds);

        if (chat.getType().equals(Availability.PRIVATE) && !participantsIds.isEmpty()) {
            ChatEntity chatEntity = chatRepository.getChat(userId, participantsIds.stream().findFirst().get());
            if (chatEntity != null) {
                return chatMapper.toChatDto(chatEntity);
            }
        }

        ChatEntity saveShat = chatRepository.save(chatMapper.toChatEntity(chat));

        participantService.addParticipants(userId, saveShat.getId(), participantsIds);
//        Set<ParticipantEntity> participantEntities = saveShat.getParticipants().stream()
//                .sorted(Comparator.comparing(ParticipantEntity::getType))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//        saveShat.setParticipants(participantEntities);
        return chatMapper.toChatDto(saveShat);
    }


    /**
     * (PRIVATE-чат) Если User-1 удаляет чат, то он будет удалён и у собеседника.
     * (GROUP-чат) Удаляется у всех участников. Может удалить только ADMIN.
     */
    @Override
    @Transactional
    public void deleteChat(Long userId, Long chatId) {
        userService.existUserById(userId);
        ChatEntity chat = getChatById(chatId);

        Set<ParticipantEntity> participants = chat.getParticipants();

        boolean isParticipant = participants.stream().anyMatch(it -> it.getKey().getUser().getId().equals(userId));
        boolean isGroupChat = chat.getType().equals(Availability.GROUP);
        boolean isAdminChat = participants.stream()
                .filter(participant -> participant.getKey().getUser().getId().equals(userId))
                .anyMatch(participant -> participant.getType().equals(TypeParticipant.ADMIN));

        if (isParticipant && isGroupChat && isAdminChat) {
            messageRepository.deleteByChatId(chatId);
            participantRepository.deleteByChatId(chatId);
            chatRepository.deleteById(chatId);
        }
    }

    @Override
    public List<ChatPreviewDto> getChatPreviews(Long userId) {
        userService.existUserById(userId);
        return convertToChatPreviewDto(chatRepository.getReviews(userId, userId));
    }

    @Override
    public ChatFullDto getChat(Long userId, Long chatId) {
        userService.existUserById(userId);
        ChatEntity chat = chatRepository.getChatByIdAndUserId(chatId, userId);

        if (chat == null) {
            throw new AccessException("No rights to this chat");
        }
        ChatFullDto chatFull = chatMapper.toChatFullDto(chat);
        if (chatFull.getType().equals(Availability.PRIVATE)) {
            Optional<String> fullName = chatFull.getParticipants().stream()
                    .filter(user -> !Objects.equals(user.getId(), userId))
                    .map(UserFullDto::getFullName)
                    .findFirst();
            chatFull.setTitle(fullName.orElse(null));
        }
        List<UserFullDto> participants = chatFull.getParticipants().stream()
                .sorted(Comparator.comparing(UserFullDto::getType))
                .collect(Collectors.toList());
        chatFull.setParticipants(participants);
        return chatFull;
    }
    //todo: определить здесь дату последнего входа!

    @Override
    public ChatEntity getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new NotFoundObjectException("Chat with ID=" + chatId + " does not exist."));
    }

    @Override
    public void existChatById(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundObjectException("Chat with ID=" + chatId + " does not exist.");
        }
    }

    private List<ChatPreviewDto> convertToChatPreviewDto(String[] source) {
        List<ChatPreviewDto> result = new ArrayList<>();

        var preview = ChatPreviewDto.builder();
        for (String element : source) {
            String[] chatPreview = element.split(",");

            preview.chatId(Long.parseLong(chatPreview[0]));
            preview.title(chatPreview[1]);
            preview.senderId(!Objects.equals(chatPreview[2], "null") ? Long.parseLong(chatPreview[2]) : null);

            if (!Objects.equals(chatPreview[3], "null")) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd HH:mm:ss")
                        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 6, true)
                        .toFormatter();
                LocalDateTime dateLastMessage = LocalDateTime.parse(chatPreview[3], formatter);

                LocalDate currentDate = LocalDate.now();
                if (dateLastMessage.toLocalDate().isEqual(currentDate)) {
                    preview.dateLastMessage(String.format("%02d:%02d:%d", dateLastMessage.getHour(), dateLastMessage.getMinute(), dateLastMessage.getSecond()));
                } else {
                    preview.dateLastMessage(String.format("%02d.%02d.%d", dateLastMessage.getDayOfMonth(), dateLastMessage.getMonthValue(), dateLastMessage.getYear()));
                }
            } else {
                preview.dateLastMessage("");
            }
            preview.stateMessage(!Objects.equals(chatPreview[2], "null") ? StateMessage.valueOf(chatPreview[4]) : null);

            String lastMessage = String.join(",", Arrays.copyOfRange(chatPreview, 5, chatPreview.length));
            if (!lastMessage.equals("null")) {
                preview.lastMessage(lastMessage);
            } else {
                preview.lastMessage("");
            }
            result.add(preview.build());
        }
        return result;
    }
}
