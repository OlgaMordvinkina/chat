package com.example.chat.services.impl;

import com.example.chat.dto.*;
import com.example.chat.dto.enums.Availability;
import com.example.chat.dto.enums.StateMessage;
import com.example.chat.dto.enums.TypeBucket;
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
import com.example.chat.services.MinioService;
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
    private final MinioService minioService;

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
    @Transactional
    public ChatDto updatePhotoChat(Long userId, Long chatId, String photo) {
        userService.existUserById(userId);
        ChatEntity chat = getChatById(chatId);

        Set<ParticipantEntity> participants = chat.getParticipants();
        boolean isParticipant = participants.stream().anyMatch(it -> it.getKey().getUser().getId().equals(userId));
        boolean isGroupChat = chat.getType().equals(Availability.GROUP);

        if (isParticipant && isGroupChat) {
            String nameFile = minioService.putFile(TypeBucket.chat.name() + chatId, photo);
            chat.setPhoto(nameFile);
            ChatDto chatDto = chatMapper.toChatDto(chatRepository.save(chat));
            chatDto.setPhoto(photo);
            return chatDto;
        }
        return null;
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
                .peek(user -> chat.getParticipants().stream()
                        .filter(it -> Objects.equals(it.getKey().getUser().getId(), user.getId()))
                        .findFirst()
                        .ifPresent(participant -> {
                            String photo = participant.getKey().getUser().getPhoto();
                            if (photo != null) {
                                String fileBase64 = getFileBase64(user.getId(), photo, TypeBucket.user.name());
                                user.setPhoto(fileBase64);
                            }
                        }))
                .collect(Collectors.toList());
        chatFull.setParticipants(participants);

        if (chat.getPhoto() != null) {
            chatFull.setPhoto(getFileBase64(chatId, chat.getPhoto(), TypeBucket.chat.name()));
        }

        List<String> files = minioService.getFiles(TypeBucket.attachmentschat.name() + chatId);
        if (!files.isEmpty()) {
            List<AttachmentDto> attachments = files.stream()
                    .map(it -> new AttachmentDto(null, it))
                    .toList();
            chatFull.setAttachments(attachments);
        }

        return chatFull;
    }

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

            long chatId = Long.parseLong(chatPreview[0]);
            preview.chatId(chatId);
            preview.messageId(!Objects.equals(chatPreview[1], "null") ? Long.parseLong(chatPreview[1]) : null);
            preview.title(chatPreview[2]);
            Long senderId = !Objects.equals(chatPreview[3], "null") ? Long.parseLong(chatPreview[3]) : null;
            preview.senderId(senderId);

            if (!Objects.equals(chatPreview[4], "null")) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd HH:mm:ss")
                        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 6, true)
                        .toFormatter();
                LocalDateTime dateLastMessage = LocalDateTime.parse(chatPreview[4], formatter);

                LocalDate currentDate = LocalDate.now();
                if (dateLastMessage.toLocalDate().isEqual(currentDate)) {
                    preview.dateLastMessage(String.format("%02d:%02d:%d", dateLastMessage.getHour(), dateLastMessage.getMinute(), dateLastMessage.getSecond()));
                } else {
                    preview.dateLastMessage(String.format("%02d.%02d.%d", dateLastMessage.getDayOfMonth(), dateLastMessage.getMonthValue(), dateLastMessage.getYear()));
                }
            } else {
                preview.dateLastMessage("");
            }
            preview.stateMessage(!Objects.equals(chatPreview[5], "null") ? StateMessage.valueOf(chatPreview[5]) : null);

            Long companionId = !Objects.equals(chatPreview[6], "null") ? Long.valueOf(chatPreview[6]) : null;
            preview.companionId(companionId);

            String nameFile = Objects.equals(chatPreview[7], "null") ? null : chatPreview[7];
            if (nameFile != null) {
                preview.photo(
                        companionId != null ?
                                getFileBase64(companionId, nameFile, TypeBucket.user.name()) :
                                getFileBase64(chatId, nameFile, TypeBucket.chat.name())
                );
            } else {
                preview.photo(null);
            }


            String lastMessage = String.join(",", Arrays.copyOfRange(chatPreview, 8, chatPreview.length));
            if (!lastMessage.equals("null")) {
                preview.lastMessage(lastMessage);
            } else {
                preview.lastMessage("");
            }

            if (senderId != null) {
                preview.unreadMessages(messageRepository.countByChat_IdAndStateAndSender_Id(chatId, StateMessage.SENT, senderId));
            } else {
                preview.unreadMessages(0L);
            }
            result.add(preview.build());
        }
        return result;
    }

    private String getFileBase64(Long id, String nameFile, String type) {
        String file = minioService.getFile(type + id, nameFile);
        if (file != null) {
            return "data:image/jpg;base64," + file;
        }
        return null;
    }
}
