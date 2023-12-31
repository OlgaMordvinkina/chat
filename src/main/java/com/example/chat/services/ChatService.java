package com.example.chat.services;

import com.example.chat.dto.AttachmentDto;
import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.entities.ChatEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {

    ChatDto createChat(Long userId, ChatDto chat);

    void deleteChat(Long userId, Long chatId);

    @Transactional
    ChatDto updatePhotoChat(Long userId, Long chatId, String photo);

    List<ChatPreviewDto> getChatPreviews(Long userId);

    ChatFullDto getChat(Long userId, Long chatId);

    List<AttachmentDto> getAttachmentsChat(Long userId, Long chatId, int page, int size);

    ChatEntity getChatById(Long chatId);

    void existChatById(Long chatId);
}
