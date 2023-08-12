package com.example.chat.utils;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.chat.repositories.ChatRepository;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.message.repositories.MessageRepository;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationsUtils {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

//    public static <T> void checkExistence(JpaRepository<T, Long> repository, NotFoundObjectException exception, Long id) {
//        if(!repository.existsById(id)) {
//            throw exception;
//        }
//    }
//
//    public void check() {
//        Long  id = 0L;
//        Function<Object, Boolean> checkById = __ -> userRepository.existsById(id);
//
//        ValidationsUtils.checkExistence(checkById, ...);
//    }
//
//    public static <T> void checkExistence(Function<Object, Boolean> function, NotFoundObjectException exception, Long id) {
//        boolean result = function.apply(null);
//
//        if(!repository.existsById(id)) {
//            throw exception;
//        }
//    }


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
