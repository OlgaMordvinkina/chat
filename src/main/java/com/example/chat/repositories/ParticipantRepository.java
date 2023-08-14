package com.example.chat.repositories;

import com.example.chat.dto.enums.TypeParticipant;
import com.example.chat.entities.CompositeKey;
import com.example.chat.entities.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, CompositeKey> {
    ParticipantEntity findByKey_Chat_IdAndType(Long chatId, TypeParticipant type);

    void deleteByKey_Chat_IdAndKey_User_Id(Long chatId, Long userId);
}
