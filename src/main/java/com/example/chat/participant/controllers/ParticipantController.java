package com.example.chat.participant.controllers;

import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.services.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats/{chatId}")
public class ParticipantController {
    private final ParticipantService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<ParticipantEntity> addParticipants(@PathVariable Long userId,
                                                   @PathVariable Long chatId,
                                                   Set<Long> participantsIds) {
        log.info("POST /users/{userId}/chats/{chatId} request received");
        return service.addParticipants(userId, chatId, participantsIds);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteParticipantById(@PathVariable Long userId,
                                      @PathVariable Long chatId,
                                      Long deletedUserId) {
        log.info("DELETE /users/{userId}/chats/{chatId} request received");
        service.deleteParticipantById(userId, chatId, deletedUserId);
    }
}
