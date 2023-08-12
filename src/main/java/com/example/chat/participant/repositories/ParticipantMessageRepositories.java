package com.example.chat.participant.repositories;

import com.example.chat.participant.entity.ParticipantMessageEntity;
import com.example.chat.participant.entity.ParticipantMessageKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantMessageRepositories extends JpaRepository<ParticipantMessageEntity, ParticipantMessageKey> {
    void deleteAllByKey_Message_Id(Long messageId);

    void deleteAllByKey_Chat_Id(Long chatId);

    void deleteAllByKey_Chat_IdAndKey_User_Id(Long chatId, Long userId);
}
