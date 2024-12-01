package org.mediagate.core.services;

import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.core.dto.ChatDto;
import org.mediagate.core.dto.ChatFullDto;
import org.mediagate.db.model.entities.ChatEntity;
import org.mediagate.db.model.ChatPreviewDto;

import java.util.List;

public interface ChatService {

    ChatDto createChat(Long userId, ChatDto chat);

    void deleteChat(Long userId, Long chatId);

    ChatDto updatePhotoChat(Long userId, Long chatId, String photo);

    List<ChatPreviewDto> getChatPreviews(Long userId);

    ChatFullDto getChat(Long userId, Long chatId);

    List<AttachmentDto> getAttachmentsChat(Long userId, Long chatId, int page, int size);

    ChatEntity getChatById(Long chatId);

    void existChatById(Long chatId);
}
