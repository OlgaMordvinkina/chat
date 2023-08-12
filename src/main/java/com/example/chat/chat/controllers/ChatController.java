package com.example.chat.chat.controllers;

import com.example.chat.chat.dto.ChatDto;
import com.example.chat.chat.dto.ChatPreviewDto;
import com.example.chat.chat.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats")
public class ChatController {
    private final ChatService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ChatDto createChat(@PathVariable Long userId, @RequestBody ChatDto newChat) {
        log.debug("POST /users/{userId}/chats request received");
        return service.createChat(userId, newChat);
    }

    @GetMapping
    public Set<ChatPreviewDto> getChatPreview(@PathVariable Long userId) {
        log.debug("GET /users/{userId}/chats request received");
        return service.getChatPreview(userId);
    }
}
