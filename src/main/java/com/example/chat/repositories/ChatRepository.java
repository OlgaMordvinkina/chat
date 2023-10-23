package com.example.chat.repositories;

import com.example.chat.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query(value = "SELECT DISTINCT c.id as chatId, m.id as messageId, " +
            "CASE WHEN c.chat_type = 'PRIVATE' THEN CONCAT(pr.surname, ' ', pr.first_name) ELSE c.title END AS title, " +
            "m.sender_id as senderId, " +
            "m.create_date as dateLastMessage, " +
            "m.state as stateMessage, " +
            "CASE WHEN c.chat_type = 'PRIVATE' THEN pr.user_id ELSE NULL END AS companionId, " +
            "CASE WHEN c.chat_type = 'PRIVATE' THEN pr.photo ELSE c.photo END AS nameFile, " +
            "m.text_message as text " +
            "FROM chats c " +
            "LEFT JOIN ( " +
            "    SELECT chat_id, MAX(create_date) AS max_create_date " +
            "    FROM messages " +
            "    GROUP BY chat_id " +
            ") m1 ON m1.chat_id = c.id " +
            "LEFT JOIN messages m ON m.chat_id = m1.chat_id AND m.create_date = m1.max_create_date " +
            "INNER JOIN participants p ON p.chat_id = c.id " +
            "LEFT JOIN ( " +
            "    SELECT chat_id, profile_id " +
            "    FROM participants " +
            "    WHERE profile_id <> ? " +
            ") p2 ON p2.chat_id = c.id " +
            "LEFT JOIN profiles pr ON pr.user_id = p2.profile_id " +
            "WHERE p.profile_id = ?;", nativeQuery = true)
    List<Map<String, Object>> getReviews(@Param("id") Long id, @Param("userId") Long userId);
//    @Query(nativeQuery = false, value = "classpath:getReviews.sql")
//    List<Map<String, Object>> getReviews(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT c " +
            "FROM ChatEntity c " +
            "JOIN c.participants pr " +
            "JOIN pr.key.user p " +
            "WHERE c.id=:chatId AND p.user.id=:userId " +
            "ORDER BY pr.type")
    ChatEntity getChatByIdAndUserId(Long chatId, Long userId);

    @Query("SELECT chat " +
            "FROM ChatEntity chat " +
            "WHERE chat.id = (SELECT p.key.chatId " +
            "FROM ChatEntity c " +
            "JOIN ParticipantEntity p ON p.key.chatId=c.id " +
            "WHERE c.type='PRIVATE' " +
            "AND (p.key.user.id = :userIdOne OR p.key.user.id = :userIdTwo) " +
            "GROUP BY p.key.chatId " +
            "HAVING count " +
            "(DISTINCT p.key.user.id) = 2)")
    ChatEntity getChat(Long userIdOne, Long userIdTwo);
}
