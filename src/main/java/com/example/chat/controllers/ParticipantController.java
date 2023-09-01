package com.example.chat.controllers;

import com.example.chat.dto.UserFullDto;
import com.example.chat.services.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/chats/{chatId}")
public class ParticipantController {
    private final ParticipantService service;

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/participants")
//    public List<ParticipantEntity> addParticipants(@PathVariable Long userId,
//                                                   @PathVariable Long chatId,
//                                                   @RequestBody Set<Long> participantsIds) {
//        log.info("POST /users/{userId}/chats/{chatId}/participants request received");
//        return service.addParticipants(userId, chatId, participantsIds);
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/participant")
    public UserFullDto addParticipant(@PathVariable Long userId,
                                      @PathVariable Long chatId,
                                      @RequestParam Long participantUserId) {
        log.info("POST /users/{userId}/chats/{chatId}/participants request received");
        return service.addParticipant(userId, chatId, participantUserId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/participants/exclude")
    public void deleteParticipantById(@PathVariable Long userId,
                                      @PathVariable Long chatId,
                                      @RequestParam Long deletedUserId) {
        log.info("DELETE /users/{userId}/chats/{chatId}/participants/exclude request received");
        service.deleteParticipantById(userId, chatId, deletedUserId);
    }
}
