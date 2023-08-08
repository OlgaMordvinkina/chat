package com.example.chat.chat.services;

import com.example.chat.chat.dto.ChatDto;

public interface ChatService {

    ChatDto createChat(Long userId, ChatDto chatDto);
}
