package com.example.chat.services;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.entities.ChatEntity;

import java.util.List;

public interface ChatService {

    ChatDto createChat(Long userId, ChatDto chat);

    void deleteChat(Long userId, Long chatId);

    List<ChatPreviewDto> getChatPreviews(Long userId);

    ChatFullDto getChat(Long userId, Long chatId);

    ChatEntity getChatById(Long chatId);

    void existChatById(Long chatId);
}
