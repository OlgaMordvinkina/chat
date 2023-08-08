package com.example.chat.participant.services;

import com.example.chat.participant.entity.ParticipantEntity;

import java.util.List;
import java.util.Set;

public interface ParticipantService {
    List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds);
}
