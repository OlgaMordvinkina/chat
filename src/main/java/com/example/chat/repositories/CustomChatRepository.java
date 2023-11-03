package com.example.chat.repositories;

import com.example.chat.dto.ChatPreviewDto;

import java.util.List;

public interface CustomChatRepository {
    List<ChatPreviewDto> getReviews(Long userId);
}
