package com.example.chat.services;

import com.example.chat.dto.MessageDto;
import com.example.chat.dto.enums.TypeSearch;

import java.util.List;

public interface MessageService {
    MessageDto createMessage(MessageDto newMessage);

    MessageDto updateMessage(MessageDto updateMessage);

    void deleteMessage(Long userId, Long chatId, Long messageId);

    List<MessageDto> getMessages(Long userId, Long chatId);

    List<MessageDto> searchMessagesChats(Long userId, Long chatId, String desired, TypeSearch type) throws IllegalAccessException;

}
