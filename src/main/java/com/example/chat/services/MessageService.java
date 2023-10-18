package com.example.chat.services;

import com.example.chat.dto.MessageDto;
import com.example.chat.dto.enums.TypeSearch;
import com.example.chat.entities.ChatEntity;

import java.util.List;

public interface MessageService {
    MessageDto createMessage(Long userId, Long chatId, MessageDto newMessage);

    MessageDto updateMessage(MessageDto updateMessage);

    MessageDto deleteMessage(Long userId, Long chatId, Long messageId);

    List<MessageDto> getMessages(Long userId, Long chatId, int page, int size);

    MessageDto getLastMessageByChat(Long chatId);

    List<MessageDto> searchMessagesChats(Long userId, Long chatId, String desired, TypeSearch type) throws IllegalAccessException;

    ChatEntity updateStateMessages(Long userId, Long chatId);
}
