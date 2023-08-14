package com.example.chat.repositories;

import com.example.chat.dto.ChatPreviewDto;

import java.util.Set;

public interface ChatCustomRepository {

    Set<ChatPreviewDto> findChatPreview(Long userId);
}
