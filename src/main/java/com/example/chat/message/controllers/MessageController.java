package com.example.chat.message.controllers;

import com.example.chat.message.dto.MessageDto;
import com.example.chat.message.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats/{chatId}/messages")
public class MessageController {
    private final MessageService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MessageDto createMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @RequestBody MessageDto newMessage) {
        log.info("POST /users/{userId}/chats/{chatId}/messages request received");
        newMessage.setSenderId(userId);
        newMessage.setChatId(chatId);
        return service.createMessage(newMessage);
    }

    @PutMapping("/{messageId}")
    public MessageDto updateMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @PathVariable Long messageId,
                                    @RequestBody MessageDto newMessage) {
        log.info("PUT /users/{userId}/chats/{chatId}/messages/{messageId} request received");
        newMessage.setId(messageId);
        newMessage.setSenderId(userId);
        newMessage.setChatId(chatId);
        return service.updateMessage(newMessage);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{messageId}")
    public void deleteMessages(@PathVariable Long userId,
                               @PathVariable Long chatId,
                               @PathVariable Long messageId) {
        log.info("DELETE /users/{userId}/chats/{chatId}/messages/{messageId} request received");
        service.deleteMessage(userId, chatId, messageId);
    }

    @GetMapping
    public List<MessageDto> getMessages(@PathVariable Long userId,
                                        @PathVariable Long chatId) {
        log.info("GET /users/{userId}/chats/{chatId}/messages request received");
        return service.getMessages(userId, chatId);
    }
}
