package org.mediagate.core.services;

import org.mediagate.core.dto.UserFullDto;
import org.mediagate.db.model.entities.ParticipantEntity;

import java.util.List;
import java.util.Set;

public interface ParticipantService {
    List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds);

    UserFullDto addParticipant(Long userId, Long chatId, Long participantUserId);

    void deleteParticipantById(Long userId, Long chatId, Long deletedUserId);
}
