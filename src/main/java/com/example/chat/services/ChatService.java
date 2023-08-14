package com.example.chat.services;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatPreviewDto;

import java.util.Set;

public interface ChatService {

    ChatDto createChat(Long userId, ChatDto chat);

    Set<ChatPreviewDto> getChatPreview(Long userId);
}
