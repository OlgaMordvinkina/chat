package com.example.chat.repositories;

import com.example.chat.dto.enums.StateMessage;
import com.example.chat.entities.MessageEntity;
import jakarta.persistence.OrderBy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByChat_Id(Long chatId, Pageable pageable);

    @OrderBy("createDate ASC")
    List<MessageEntity> findAllByIdInOrderById(Set<Long> ids);

    @Query("SELECT m " +
            "FROM MessageEntity m " +
            "JOIN ChatEntity c on c.id=m.chat.id " +
            "JOIN c.participants pr " +
            "JOIN pr.key.user p " +
            "WHERE p.user.id=:userId " +
            "AND lower(m.text) LIKE lower(CONCAT('%', :desired, '%'))")
    List<MessageEntity> searchMessagesAllChats(Long userId, String desired);

    @Query("SELECT m " +
            "FROM MessageEntity m " +
            "JOIN ChatEntity c on c.id=m.chat.id " +
            "JOIN c.participants pr " +
            "JOIN pr.key.user p " +
            "WHERE p.user.id=:userId AND c.id=:chatId " +
            "AND lower(m.text) LIKE lower(CONCAT('%', :desired, '%'))")
    List<MessageEntity> searchMessagesThisChat(Long userId, Long chatId, String desired);

    void deleteByChatId(Long chatId);

    Long countByChat_IdAndStateAndSender_Id(Long chatId, StateMessage state, Long senderId);

    @Modifying
    @Query(value = "UPDATE messages " +
            "SET state = 'READ' " +
            "WHERE chat_id=:chatId " +
            "AND sender_id!=:userId " +
            "AND state = 'SENT'", nativeQuery = true)
    void updateStateMessage(@Param("chatId") Long chatId, @Param("userId") Long userId);

    @Query("SELECT m " +
            "FROM MessageEntity m " +
            "WHERE m.chat.id = :chatId " +
            "ORDER BY m.createDate DESC " +
            "LIMIT 1")
    MessageEntity findLastByChatId(Long chatId);

    @Modifying
    @Query(value = "UPDATE messages " +
            "SET reply_message_id = 0 " +
            "WHERE reply_message_id=:messageId ", nativeQuery = true)
    void updateReplyMessage(@Param("messageId") Long messageId);
}
