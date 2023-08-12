package com.example.chat.message.services;

import com.example.chat.message.dto.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto createMessage(MessageDto newMessage);

    MessageDto updateMessage(MessageDto updateMessage);

    void deleteMessage(Long userId, Long chatId, Long messageId);

    List<MessageDto> getMessages(Long userId, Long chatId);
}
