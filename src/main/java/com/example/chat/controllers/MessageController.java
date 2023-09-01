package com.example.chat.controllers;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.MessageDto;
import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.enums.TypeSearch;
import com.example.chat.services.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats")
public class MessageController {
    private final MessageService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{chatId}/messages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MessageDto createMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @RequestBody MessageDto newMessage) {
        log.info("POST /users/{userId}/chats/{chatId}/messages request received");
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId(userId);
        newMessage.setSender(profileDto);
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chatId);
        newMessage.setChat(chatDto);
        return service.createMessage(newMessage);
    }

    @PutMapping("/{chatId}/messages")
    public MessageDto updateMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @RequestBody MessageDto newMessage) {
        log.info("PUT /users/{userId}/chats/{chatId}/messages/{messageId} request received");
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId(userId);
        newMessage.setSender(profileDto);
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chatId);
        newMessage.setChat(chatDto);
        return service.updateMessage(newMessage);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{chatId}/messages/{messageId}")
    public void deleteMessage(@PathVariable Long userId,
                              @PathVariable Long chatId,
                              @PathVariable Long messageId) {
        log.info("DELETE /users/{userId}/chats/{chatId}/messages/{messageId} request received");
        service.deleteMessage(userId, chatId, messageId);
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long userId,
                                        @PathVariable Long chatId) {
        log.info("GET /users/{userId}/chats/{chatId}/messages request received");
        return service.getMessages(userId, chatId);
    }

    @GetMapping("/messages/search")
    List<MessageDto> searchMessagesAllChats(@PathVariable Long userId,
                                            @Valid
                                            @Min(1)
                                            @NonNull
                                            @NotBlank
                                            @Pattern(regexp = "^\\S*$")
                                            @RequestParam String desired) throws IllegalAccessException {
        log.debug("GET /users/{userId}/chats/messages/search request received");
        return service.searchMessagesChats(userId, null, desired, TypeSearch.ALL_CHATS);
    }

    @GetMapping("/{chatId}/messages/search")
    List<MessageDto> searchMessagesThisChat(@PathVariable Long userId,
                                            @PathVariable Long chatId,
                                            @Valid
                                            @Min(1)
                                            @NonNull
                                            @NotBlank
                                            @Pattern(regexp = "^\\S*$")
                                            @RequestParam String desired) throws IllegalAccessException {
        log.debug("GET /users/{userId}/chats/{chatId}/messages/search request received");
        return service.searchMessagesChats(userId, chatId, desired, TypeSearch.THIS_CHAT);
    }

//TODO: добавить секьюрити
//@GetMapping("/{chatId}/messages")
//public List<MessageDto> getMessages(@PathVariable Long userId,
//                                    @PathVariable Long chatId,
//                                    Principal user
//) {
//    log.debug("GET /users/{userId}/chats/{chatId}/messages request received");
//    return service.getMessages(userId, chatId);
//}
}
