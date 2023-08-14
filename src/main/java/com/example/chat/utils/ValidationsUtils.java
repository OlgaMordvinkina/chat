package com.example.chat.utils;

import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.MessageEntity;
import com.example.chat.entities.UserEntity;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.repositories.ChatRepository;
import com.example.chat.repositories.MessageRepository;
import com.example.chat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationsUtils {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundObjectException("User with ID=" + userId + " does not exist."));
    }

    public ChatEntity getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new NotFoundObjectException("Chat with ID=" + chatId + " does not exist."));
    }

    public MessageEntity getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundObjectException("Message with ID=" + messageId + " does not exist."));
    }

    public void existUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundObjectException("User with ID=" + userId + " does not exist.");
        }
    }

    public void existChatById(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NotFoundObjectException("Chat with ID=" + chatId + " does not exist.");
        }
    }

    public static boolean checkForNullOrEmpty(Object obj) {
        if (obj == null) {
            return false;
        } else {
            return !obj.toString().isEmpty();
        }
    }
}
