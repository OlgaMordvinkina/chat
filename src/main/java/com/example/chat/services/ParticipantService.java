package com.example.chat.services;

import com.example.chat.dto.UserFullDto;
import com.example.chat.entities.ParticipantEntity;

import java.util.List;
import java.util.Set;

public interface ParticipantService {
    List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds);

    UserFullDto addParticipant(Long userId, Long chatId, Long participantUserId);

    void deleteParticipantById(Long userId, Long chatId, Long deletedUserId);
}
