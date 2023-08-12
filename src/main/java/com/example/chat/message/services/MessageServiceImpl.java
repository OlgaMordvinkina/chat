package com.example.chat.message.services;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.message.dto.MessageDto;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.message.enums.StateMessage;
import com.example.chat.message.mapper.MessageMapper;
import com.example.chat.message.repositories.MessageRepository;
import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.services.ParticipantService;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.utils.ValidationsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ParticipantService participantService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ValidationsUtils utils;

    @Override
    public MessageDto createMessage(MessageDto newMessage) {
        UserEntity sender = utils.getUserById(newMessage.getSenderId());
        ChatEntity chat = utils.getChatById(newMessage.getChatId());
        extendParticipant(newMessage.getSenderId(), chat.getParticipants());
        newMessage.setState(StateMessage.SENT);
        newMessage.setCreateDate(LocalDateTime.now());
        MessageEntity message = messageMapper.toMessageEntity(newMessage, sender, chat);
        message.setChat(chat);
        MessageEntity saveMessage = messageRepository.save(message);
        if (ValidationsUtils.checkForNullOrEmpty(chat.getParticipants())) chat.getParticipants().forEach(participant ->
                participantService.addMessagesToParticipant(participant.getKey().getUser(), chat, saveMessage));
        return messageMapper.toMessageDto(saveMessage);
    }

    @Override
    public MessageDto updateMessage(MessageDto updateMessage) {
        utils.existUserById(updateMessage.getSenderId());
        ChatEntity chat = utils.getChatById(updateMessage.getChatId());
        extendParticipant(updateMessage.getSenderId(), chat.getParticipants());

        MessageEntity oldMessage = utils.getMessageById(updateMessage.getId());
        extendSender(oldMessage.getSender().getId(), updateMessage.getSenderId());

        oldMessage.setText(updateMessage.getText());
        return messageMapper.toMessageDto(messageRepository.save(oldMessage));
    }

    @Override
    public void deleteMessage(Long userId, Long chatId, Long messageId) {
        utils.existChatById(chatId);
        MessageEntity oldMessage = utils.getMessageById(messageId);

        extendSender(oldMessage.getSender().getId(), userId);
        participantService.deleteMessagesToParticipantByMessageId(messageId);
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageDto> getMessages(Long userId, Long chatId) {
        utils.existUserById(userId);
        ChatEntity chat = utils.getChatById(chatId);
        extendParticipant(userId, chat.getParticipants());

        List<MessageEntity> messages = messageRepository.findAllByChat_Id(chatId);
        return messages.size() > 0 ? messages.stream()
                .map(messageMapper::toMessageDto)
                .toList() : List.of();
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
}
