package org.mediagate.core.services.impl;

import org.mediagate.auth.service.KeycloakService;
import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.core.dto.ChatDto;
import org.mediagate.core.dto.ChatFullDto;
import org.mediagate.core.dto.UserFullDto;
import org.mediagate.auth.model.Groups;
import org.mediagate.core.dto.enums.TypeBucket;
import org.mediagate.core.exceptions.AccessException;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.mediagate.core.mappers.ChatMapper;
import org.mediagate.core.services.ChatService;
import org.mediagate.core.services.MinioService;
import org.mediagate.core.services.ParticipantService;
import org.mediagate.core.services.UserService;
import org.mediagate.db.model.entities.AttachmentEntity;
import org.mediagate.db.model.entities.ChatEntity;
import org.mediagate.db.model.entities.ParticipantEntity;
import org.mediagate.db.enums.Availability;
import org.mediagate.db.enums.StateMessage;
import org.mediagate.db.enums.TypeParticipant;
import org.mediagate.db.model.ChatPreviewDto;
import lombok.RequiredArgsConstructor;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AttachmentRepository attachmentRepository;
    private final ParticipantRepository participantRepository;
    private final ChatMapper chatMapper;
    private final MinioService minioService;
    private final KeycloakService keycloakService;

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
        List<ChatPreviewDto> previews = chatRepository.getReviews(userId);
        previews.forEach(preview -> {

            String photo = preview.getPhoto();
            if (photo != null) {
                preview.setPhoto(null);
//                Long companionId = preview.getCompanionId();
//                preview.setPhoto(
//                        companionId != null ?
//                                getUrlFiles(companionId, photo, TypeBucket.user.name()) :
//                                getUrlFiles(preview.getChatId(), photo, TypeBucket.chat.name())
//                );
            }

            Long senderId = preview.getSenderId();
            if (senderId != null) {
                preview.setUnreadMessages(messageRepository.countByChat_IdAndStateAndSender_Id(
                        preview.getChatId(),
                        StateMessage.SENT,
                        senderId));
            }
        });
        return previews;
    }

    @Override
    public ChatFullDto getChat(Long userId, Long chatId) {
//        userService.existUserById(userId);
        UserEntity user = userRepository.findUserById(userId);
        ChatEntity chat = chatRepository.getChatByIdAndUserId(chatId, userId);

        if (chat == null) {
            throw new AccessException("Нет прав на этот чат");
        }
        List<String> groups = keycloakService.getUserByEmail(user.getEmail()).getGroups();
        if (groups == null || groups.stream().noneMatch(group -> group.equals(Groups.group_chats.name()))) {
            throw new AccessException("У пользователя отсутствует доступ к групповым чатам");
        }

        ChatFullDto chatFull = chatMapper.toChatFullDto(chat);
        if (chatFull.getType().equals(Availability.PRIVATE)) {
            Optional<String> fullName = chatFull.getParticipants().stream()
                    .filter(userFullDto -> !Objects.equals(userFullDto.getId(), userId))
                    .map(UserFullDto::getFullName)
                    .findFirst();
            chatFull.setTitle(fullName.orElse(null));
        }

        List<UserFullDto> participants = chatFull.getParticipants().stream()
                .peek(userFullDto -> chat.getParticipants().stream()
                        .filter(it -> Objects.equals(it.getKey().getUser().getId(), userFullDto.getId()))
                        .findFirst()
                        .ifPresent(participant -> {
                            String photo = participant.getKey().getUser().getPhoto();
                            if (photo != null) {
                                // todo костыль
                                userFullDto.setPhoto(null);
//                                String fileBase64 = getUrlFiles(user.getId(), photo, TypeBucket.user.name());
//                                user.setPhoto(fileBase64);
                            }
                        }))
                .collect(Collectors.toList());
        chatFull.setParticipants(participants);

        if (chat.getPhoto() != null) {
            // todo костыль
            chatFull.setPhoto(null);
//            chatFull.setPhoto(getUrlFiles(chatId, chat.getPhoto(), TypeBucket.chat.name()));
        }
        return chatFull;
    }

    @Override
    public List<AttachmentDto> getAttachmentsChat(Long userId, Long chatId, int page, int size) {
        Pageable pages = PageRequest.of(--page, size);
        List<AttachmentEntity> attachments = attachmentRepository.findByChatId(chatId, pages);

        List<AttachmentDto> files = new ArrayList<>();
        attachments.forEach(it -> {
            var dto = AttachmentDto.builder();
            dto.id(it.getId());
            dto.file(minioService.getUrlFiles(TypeBucket.attachmentschat.name() + chatId, it.getNameFile()));
            files.add(dto.build());
        });
        if (!files.isEmpty()) {
            return files;
        }
        return null;
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

    private String getUrlFiles(Long id, String nameFile, String type) {
        return minioService.getUrlFiles(type + id, nameFile);
    }
}
