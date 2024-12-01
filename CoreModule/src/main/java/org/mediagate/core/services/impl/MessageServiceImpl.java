package org.mediagate.core.services.impl;

import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.core.dto.MessageDto;
import org.mediagate.core.dto.enums.TypeBucket;
import org.mediagate.core.dto.enums.TypeSearch;
import org.mediagate.core.exceptions.AccessException;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.mediagate.core.mappers.AttachmentMapper;
import org.mediagate.core.mappers.MessageMapper;
import org.mediagate.core.services.*;
import org.mediagate.db.enums.Availability;
import org.mediagate.db.enums.StateMessage;
import org.mediagate.db.model.entities.*;
import org.mediagate.db.repositories.AttachmentRepository;
import org.mediagate.db.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final ChatService chatService;
    private final ProfileService profileService;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;
    private final MessageMapper messageMapper;
    private final MinioService minioService;
    private final AttachmentMapper attachmentMapper;

    @Override
    public MessageDto createMessage(Long userId, Long chatId, MessageDto newMessage) {
        ProfileEntity sender = profileService.findProfileByUserId(userId);
        ChatEntity chat = chatService.getChatById(chatId);
        isParticipant(userId, chat.getParticipants());

        newMessage.setState(StateMessage.SENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        newMessage.setCreateDate(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));

        MessageEntity replyMessageId = newMessage.getReplyMessage() != null
                ? getMessageById(newMessage.getReplyMessage().getId())
                : null;

        List<MessageEntity> forwardFrom = newMessage.getForwardedFrom() != null
                ? messageRepository.findAllByIdInOrderById(newMessage.getForwardedFrom().stream().map(MessageDto::getId).collect(Collectors.toSet()))
                : null;

        MessageEntity message = messageMapper.toMessageEntity(newMessage, sender, chat, replyMessageId, forwardFrom);

        List<AttachmentEntity> attachments = new ArrayList<>();
        if (newMessage.getAttachments() != null) {
            newMessage.getAttachments().forEach(attachment -> {
                AttachmentEntity entity = new AttachmentEntity();
                String nameAttachment = minioService.putFile(TypeBucket.attachmentschat.name() + chatId.toString(), attachment.getFile());
                entity.setNameFile(nameAttachment);
                attachments.add(attachmentRepository.save(entity));
            });
            message.setAttachments(attachments);
        }

        return messageMapper.toMessageDto(messageRepository.save(message), newMessage.getAttachments());
    }

    @Override
    public MessageDto updateMessage(MessageDto updateMessage) {
        userService.existUserById(updateMessage.getSender().getUserId());
        ChatEntity chat = chatService.getChatById(updateMessage.getChat().getId());
        isParticipant(updateMessage.getSender().getUserId(), chat.getParticipants());

        MessageEntity oldMessage = getMessageById(updateMessage.getId());
        isSender(oldMessage.getSender().getId(), updateMessage.getSender().getUserId());

        oldMessage.setText(updateMessage.getText());

        if (updateMessage.getState() != null) {
            oldMessage.setState(updateMessage.getState());
        }
        return messageMapper.toMessageDto(messageRepository.save(oldMessage), null);
    }

    @Override
    public MessageDto deleteMessage(Long userId, Long chatId, Long messageId) {
        chatService.existChatById(chatId);
        MessageEntity oldMessage = getMessageById(messageId);

        isSender(oldMessage.getSender().getId(), userId);

        if (oldMessage.getAttachments() != null) {
            oldMessage.getAttachments().forEach(it -> minioService.removeFile(TypeBucket.attachmentschat.name() + chatId, it.getNameFile()));
        }

        messageRepository.updateReplyMessage(messageId);
        messageRepository.deleteById(messageId);
        return messageMapper.toMessageDto(oldMessage, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long userId, Long chatId, int page, int size) {
        userService.existUserById(userId);
        ChatEntity chat = chatService.getChatById(chatId);
        isParticipant(userId, chat.getParticipants());

        int pageNumber = page - 1;
        Pageable pages = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "id"));
        List<MessageDto> messages = toListMessageDto(chatId, messageRepository.findAllByChat_Id(chatId, pages));
        messages.forEach(message -> {
            String photo = message.getSender().getPhoto();
            message.getSender().setPhoto(photo != null ? minioService.getUrlFiles(TypeBucket.user.name() + message.getSender().getId(), photo) : null);
        });
        return messages;
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto getLastMessageByChat(Long chatId) {
        return messageMapper.toMessageDto(messageRepository.findLastByChatId(chatId), null);
    }

    @Override
    public List<MessageDto> searchMessagesChats(Long userId, Long chatId, String desired, TypeSearch type) throws IllegalAccessException {
        if (desired.isBlank()) {
            throw new IllegalAccessException();
        }
        List<MessageEntity> messages = new ArrayList<>();

        if (type.equals(TypeSearch.THIS_CHAT)) {
            messages = messageRepository.searchMessagesThisChat(userId, chatId, desired);
        } else if (type.equals(TypeSearch.ALL_CHATS)) {
            messages = messageRepository.searchMessagesAllChats(userId, desired);
        }
        if (messages.size() > 0) {
            messages.forEach(message -> {
                if (message.getChat().getType().equals(Availability.PRIVATE)) {
                    String title = String.format("%s %s", message.getSender().getSurname(), message.getSender().getName());
                    message.getChat().setTitle(title);
                }
            });
        }

        return toListMessageDto(chatId, messages);
    }

    @Override
    public ChatEntity updateStateMessages(Long userId, Long chatId) {
        userService.existUserById(userId);
        ChatEntity chat = chatService.getChatById(chatId);
        isParticipant(userId, chat.getParticipants());
        messageRepository.updateStateMessage(chatId, userId);
        return chat;
    }

    @Transactional(readOnly = true)
    private MessageEntity getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundObjectException("Message with ID=" + messageId + " does not exist."));
    }

    private void isSender(Long senderId, Long userId) {
        if (!senderId.equals(userId)) {
            throw new AccessException("No rights to edit this post");
        }
    }

    private void isParticipant(Long userId, Set<ParticipantEntity> participants) {
        if (participants != null && participants.stream()
                .noneMatch(it -> it.getKey().getUser().getId().equals(userId))) {
            throw new AccessException("No rights to write to this chat");
        }
    }

    private List<MessageDto> toListMessageDto(Long chatId, List<MessageEntity> messages) {
        List<MessageDto> msg = messages.stream()
                .map(message -> {
                    List<AttachmentDto> attachmentDtos = message.getAttachments() != null ?
                            message.getAttachments().stream().map(attachmentMapper::toAttachmentDto).toList() :
                            List.of();
                    return messageMapper.toMessageDto(message, attachmentDtos);
                })
                .toList();

        msg.forEach(message -> {
                    List<AttachmentDto> attachments = new ArrayList<>();
                    message.getAttachments().forEach(attachment -> {
                        String fileBase64 = minioService.getUrlFiles(TypeBucket.attachmentschat.name() + chatId, attachment.getFile());
                        attachments.add(new AttachmentDto(attachment.getId(), fileBase64));
                    });

                    message.setAttachments(attachments);
                }
        );

        return messages.size() > 0 ? msg : List.of();
    }
}
