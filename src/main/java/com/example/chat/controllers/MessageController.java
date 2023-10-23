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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("checkstyle:Regexp")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats")
public class MessageController {
    private final MessageService service;
    private final SimpMessagingTemplate messagingTemplate;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{chatId}/messages")
    public MessageDto createMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @RequestBody MessageDto newMessage) {
        log.info("POST /users/" + userId + "/chats/" + chatId + "/messages request received");
        MessageDto createdMessage = service.createMessage(userId, chatId, newMessage);

        sendToWebsocket(createdMessage, "CREATE");

        return createdMessage;
    }

    @PutMapping("/{chatId}/messages")
    public MessageDto updateMessage(@PathVariable Long userId,
                                    @PathVariable Long chatId,
                                    @RequestBody MessageDto newMessage) {
        log.info("PUT /users/" + userId + "/chats/" + chatId + "/messages request received");
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId(userId);
        newMessage.setSender(profileDto);
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chatId);
        newMessage.setChat(chatDto);
        MessageDto updatedMessage = service.updateMessage(newMessage);
        sendToWebsocket(updatedMessage, "UPDATE");

        return updatedMessage;
    }

    @PutMapping("/{chatId}/messages/state")
    public void updateStateMessage(@PathVariable Long userId,
                                   @PathVariable Long chatId) {
        log.info("PUT /users/" + userId + "/chats/" + chatId + "/messages/state request received");

        service.updateStateMessages(userId, chatId);
        sendToWebsocket(chatId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{chatId}/messages/{messageId}")
    public void deleteMessage(@PathVariable Long userId,
                              @PathVariable Long chatId,
                              @PathVariable Long messageId) {
        log.info("DELETE /users/" + userId + "/chats/" + chatId + "/messages/" + messageId + " request received");
        MessageDto message = service.deleteMessage(userId, chatId, messageId);
        sendToWebsocket(message, "DELETE");
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long userId,
                                        @PathVariable Long chatId,
                                        @RequestParam(defaultValue = "1") @Positive int page,
                                        @RequestParam(defaultValue = "15") @Positive int size) {
        log.info("GET /users/" + userId + "/chats/" + chatId + "/messages?page=" + page + "?size=" + size + " request received");
        return service.getMessages(userId, chatId, page, size);
    }

    @GetMapping("/messages/search")
    List<MessageDto> searchMessagesAllChats(@PathVariable Long userId,
                                            @Valid
                                            @Min(1)
                                            @NonNull
                                            @NotBlank
                                            @Pattern(regexp = "^\\S*$")
                                            @RequestParam String desired) throws IllegalAccessException {
        log.debug("GET /users/" + userId + "/chats/messages/search request received");
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
        log.debug("GET /users/" + userId + "/chats/" + chatId + "/messages/search request received");
        return service.searchMessagesChats(userId, chatId, desired, TypeSearch.THIS_CHAT);
    }

    private void sendToWebsocket(MessageDto message, String type) {
        if (message.getChat().getParticipants() != null) {
            message.getChat().getParticipants().stream()
                    .filter(it -> !Objects.equals(it, message.getSender().getId()))
                    .forEach(it -> {
                                message.setTypeMessage(type);

                                messagingTemplate.convertAndSendToUser(
                                        it.toString(),
                                        "/messages",
                                        message
                                );
                            }
                    );
        }
    }

    private void sendToWebsocket(Long chatId) {
        MessageDto lastMessage = service.getLastMessageByChat(chatId);
        if (lastMessage != null) {
            messagingTemplate.convertAndSendToUser(
                    lastMessage.getSender().getUserId().toString(),
                    "/messages/state",
                    chatId
            );
        }
    }
}
