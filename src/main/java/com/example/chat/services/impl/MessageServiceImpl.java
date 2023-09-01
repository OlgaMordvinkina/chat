package com.example.chat.services.impl;

import com.example.chat.dto.MessageDto;
import com.example.chat.dto.enums.Availability;
import com.example.chat.dto.enums.StateMessage;
import com.example.chat.dto.enums.TypeSearch;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.MessageEntity;
import com.example.chat.entities.ParticipantEntity;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.mappers.MessageMapper;
import com.example.chat.repositories.MessageRepository;
import com.example.chat.services.ChatService;
import com.example.chat.services.MessageService;
import com.example.chat.services.ProfileService;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final ChatService chatService;
    private final ProfileService profileService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto createMessage(MessageDto newMessage) {
        ProfileEntity sender = profileService.findProfileByUserId(newMessage.getSender().getUserId());
        ChatEntity chat = chatService.getChatById(newMessage.getChat().getId());
        extendParticipant(newMessage.getSender().getUserId(), chat.getParticipants());

        newMessage.setState(StateMessage.SENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        newMessage.setCreateDate(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));

        MessageEntity replyMessageId = newMessage.getReplyMessageId() != null
                ? getMessageById(newMessage.getReplyMessageId())
                : null;

        List<MessageEntity> forwardFromIds = newMessage.getForwardFromIds() != null
                ? messageRepository.findAllByIdInOrderById(newMessage.getForwardFromIds())
                : null;

        MessageEntity message = messageMapper.toMessageEntity(newMessage, sender, chat, replyMessageId, forwardFromIds);
        MessageEntity saveMessage = messageRepository.save(message);
        return messageMapper.toMessageDto(saveMessage);
    }

    @Override
    public MessageDto updateMessage(MessageDto updateMessage) {
        userService.existUserById(updateMessage.getSender().getUserId());
        ChatEntity chat = chatService.getChatById(updateMessage.getChat().getId());
        extendParticipant(updateMessage.getSender().getUserId(), chat.getParticipants());

        MessageEntity oldMessage = getMessageById(updateMessage.getId());
        extendSender(oldMessage.getSender().getId(), updateMessage.getSender().getUserId());

        oldMessage.setText(updateMessage.getText());
        return messageMapper.toMessageDto(messageRepository.save(oldMessage));
    }

    @Override
    public void deleteMessage(Long userId, Long chatId, Long messageId) {
        chatService.existChatById(chatId);
        MessageEntity oldMessage = getMessageById(messageId);

        extendSender(oldMessage.getSender().getId(), userId);
        messageRepository.deleteById(messageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(Long userId, Long chatId) {
        userService.existUserById(userId);
        ChatEntity chat = chatService.getChatById(chatId);
        extendParticipant(userId, chat.getParticipants());

        List<MessageEntity> messages = messageRepository.findAllByChat_IdOrderById(chatId);
        return toListMessageDto(messages);
    }

//    @Override
//    public List<MessageDto> searchMessagesAllChats(Long userId, String desired) throws IllegalAccessException {
//        if (desired.isBlank()) {
//            throw new IllegalAccessException();
//        }
//        List<MessageEntity> messages = messageRepository.searchMessagesAllChats(userId, desired);
//        messages.forEach(message -> {
//            if (message.getChat().getType().equals(Availability.PRIVATE)) {
//                String title = String.format("%s %s", message.getSender().getSurname(), message.getSender().getName());
//                message.getChat().setTitle(title);
//            }
//        });
//
//        return toListMessageDto(messages);
//    }
//
//    @Override
//    public List<MessageDto> searchMessagesThisChat(Long userId, Long chatId, String desired) throws IllegalAccessException {
//        if (desired.isBlank()) {
//            throw new IllegalAccessException();
//        }
//        List<MessageEntity> messages = messageRepository.searchMessagesThisChat(userId, chatId, desired);
//        messages.forEach(message -> {
//            if (message.getChat().getType().equals(Availability.PRIVATE)) {
//                String title = String.format("%s %s", message.getSender().getSurname(), message.getSender().getName());
//                message.getChat().setTitle(title);
//            }
//        });
//
//        return toListMessageDto(messages);
//    }

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

        return toListMessageDto(messages);
    }

    @Transactional(readOnly = true)
    public MessageEntity getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundObjectException("Message with ID=" + messageId + " does not exist."));
    }

    private void extendSender(Long senderId, Long userId) {
        if (!senderId.equals(userId)) {
            throw new AccessException("No rights to edit this post");
        }
    }

    private void extendParticipant(Long userId, Set<ParticipantEntity> participants) {
        if (participants != null && participants.stream()
                .noneMatch(it -> it.getKey().getUser().getId().equals(userId))) {
            throw new AccessException("No rights to write to this chat");
        }
    }

    private List<MessageDto> toListMessageDto(List<MessageEntity> messages) {
        return messages.size() > 0 ? messages.stream()
                .map(messageMapper::toMessageDto)
                .toList() : List.of();
    }
}
