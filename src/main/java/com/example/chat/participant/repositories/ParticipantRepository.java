package com.example.chat.participant.repositories;

import com.example.chat.participant.entity.CompositeKey;
import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.enums.TypeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, CompositeKey> {
    ParticipantEntity findByKey_Chat_IdAndType(Long chatId, TypeParticipant type);
}
