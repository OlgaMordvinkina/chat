package com.example.chat.controllers;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        log.debug("POST /users/{userId}/chats request received");
        return service.createChat(userId, newChat);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable Long userId,
                    @PathVariable Long chatId) {
        log.debug("DELETE /users/{userId}/chats/{chatId} request received");
        service.deleteChat(userId, chatId);
    }

    @GetMapping("/previews")
    public List<ChatPreviewDto> getChatPreviews(@PathVariable Long userId) {
        log.debug("GET /users/{userId}/chats/previews request received");
        return service.getChatPreviews(userId);
    }

    @GetMapping("/{chatId}")
    public ChatFullDto getChat(@PathVariable Long userId,
                               @PathVariable Long chatId) {
        log.debug("GET /users/{userId}/chats/{chatId} request received");
        return service.getChat(userId, chatId);
    }

    @PutMapping("/{chatId}")
    public ChatDto updatePhotoChat(@PathVariable Long userId,
                                   @PathVariable Long chatId,
                                   @RequestBody String photo) {
        log.debug("PUT /users/{userId}/chats/{chatId} request received");
        return service.updatePhotoChat(userId, chatId, photo);
    }
}
