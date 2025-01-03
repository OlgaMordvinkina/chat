package org.mediagate.core.controllers;

import org.mediagate.core.dto.UserFullDto;
import org.mediagate.core.services.ParticipantService;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/participant")
    public UserFullDto addParticipant(@PathVariable Long userId,
                                      @PathVariable Long chatId,
                                      @RequestParam Long participantUserId) {
        log.info("POST /users/" + userId + "/chats/" + chatId + "/participants получен запрос");
        return service.addParticipant(userId, chatId, participantUserId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/participants/exclude")
    public void deleteParticipantById(@PathVariable Long userId,
                                      @PathVariable Long chatId,
                                      @RequestParam Long deletedUserId) {
        log.info("DELETE /users/" + userId + "/chats/" + chatId + "/participants/exclude получен запрос");
        service.deleteParticipantById(userId, chatId, deletedUserId);
    }
}
