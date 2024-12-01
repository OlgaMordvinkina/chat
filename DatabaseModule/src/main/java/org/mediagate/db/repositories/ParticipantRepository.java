package org.mediagate.db.repositories;

import org.mediagate.db.model.entities.CompositeKey;
import org.mediagate.db.model.entities.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, CompositeKey> {
    void deleteByKey_ChatIdAndKey_User_Id(Long chatId, Long userId);

    @Modifying
    @Query("DELETE FROM ParticipantEntity p WHERE p.key.chatId = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);
}
