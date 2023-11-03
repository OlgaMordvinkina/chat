package com.example.chat.repositories;

import com.example.chat.entities.AttachmentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
//    List<AttachmentEntity> findByChatId(Long chatId, Pageable pages);

    //    @Query("SELECT a " +
//            "FROM AttachmentEntity a " +
//            "JOIN a.messageId m " +
//            "WHERE m.chat.id = :chatId")
    @Query(value = "select a.* " +
            "from attachments as a " +
            "join messages m ON m.id = a.message_id " +
            "where m.chat_id=?", nativeQuery = true)
    List<AttachmentEntity> findByChatId(Long chatId, Pageable pages);
}
