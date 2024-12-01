package org.mediagate.core.controllers;

import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.core.dto.ChatDto;
import org.mediagate.core.dto.ChatFullDto;
import org.mediagate.db.model.ChatPreviewDto;
import org.mediagate.core.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats")
public class ChatController {
    private final ChatService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ChatDto createChat(@PathVariable Long userId,
                              @RequestBody ChatDto newChat) {
        log.debug("POST /users/" + userId + "/chats request received");
        return service.createChat(userId, newChat);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable Long userId,
                           @PathVariable Long chatId) {
        log.debug("DELETE /users/" + userId + "/chats/" + chatId + " request received");
        service.deleteChat(userId, chatId);
    }

    @GetMapping("/previews")
    public List<ChatPreviewDto> getChatPreviews(@PathVariable Long userId) {
        log.debug("GET /users/" + userId + "/chats/previews request received");
        return service.getChatPreviews(userId);
    }

    @GetMapping("/{chatId}")
    public ChatFullDto getChat(@PathVariable Long userId,
                               @PathVariable Long chatId) {
        log.debug("GET /users/" + userId + "/chats/" + chatId + " request received");
        return service.getChat(userId, chatId);
    }

    @GetMapping("/{chatId}/attachments")
    public List<AttachmentDto> getAttachmentsChat(@PathVariable Long userId,
                                                  @PathVariable Long chatId,
                                                  @RequestParam(defaultValue = "1") @Positive int page,
                                                  @RequestParam(defaultValue = "12") @Positive int size) {
        log.info("GET /users/" + userId + "/chats/" + chatId + "attachments?page=" + page + "?size=" + size + " request received");
        return service.getAttachmentsChat(userId, chatId, page, size);
    }

    @PutMapping("/{chatId}")
    public ChatDto updatePhotoChat(@PathVariable Long userId,
                                   @PathVariable Long chatId,
                                   @RequestBody String photo) {
        log.debug("PUT /users/" + userId + "/chats/" + chatId + " request received");
        return service.updatePhotoChat(userId, chatId, photo);
    }
}
