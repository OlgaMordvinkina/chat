package com.example.chat.services;

import com.example.chat.entities.ParticipantEntity;

import java.util.List;
import java.util.Set;

public interface ParticipantService {
    List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds);

    void deleteParticipantById(Long userId, Long chatId, Long deletedUserId);
}
