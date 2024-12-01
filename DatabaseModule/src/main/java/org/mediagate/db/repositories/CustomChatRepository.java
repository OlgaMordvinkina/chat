package org.mediagate.db.repositories;

import org.mediagate.db.model.ChatPreviewDto;

import java.util.List;

public interface CustomChatRepository {
    List<ChatPreviewDto> getReviews(Long userId);
}
