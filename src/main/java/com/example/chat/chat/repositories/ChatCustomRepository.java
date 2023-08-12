package com.example.chat.chat.repositories;

import com.example.chat.chat.dto.ChatPreviewDto;

import java.util.Set;

public interface ChatCustomRepository {

    Set<ChatPreviewDto> findChatPreview(Long userId);
}
