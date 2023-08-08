package com.example.chat.message.services;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.chat.repositories.ChatRepository;
import com.example.chat.message.dto.MessageDto;
import com.example.chat.message.enums.StateMessage;
import com.example.chat.message.exception.AccessException;
import com.example.chat.message.exception.NotFoundChatException;
import com.example.chat.message.mapper.MessageMapper;
import com.example.chat.message.repositories.MessageRepository;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.user.exceptions.NotFoundUserException;
import com.example.chat.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto createMessage(MessageDto newMessage) {
        UserEntity sender = getUserById(newMessage.getSenderId());
        ChatEntity chat = getChatById(newMessage.getChatId());
        chat.getParticipants().stream()
                .filter(it -> Objects.equals(it.getKey().getUser().getId(), newMessage.getSenderId()))
                .findFirst()
                .orElseThrow(() -> new AccessException("No rights to write to this chat"));
        newMessage.setState(StateMessage.SENT);

        return messageMapper.toMessageDto(messageRepository.save(messageMapper.toMessageEntity(newMessage, sender, chat)));
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
    }

    private ChatEntity getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new NotFoundChatException(chatId));
    }
}
