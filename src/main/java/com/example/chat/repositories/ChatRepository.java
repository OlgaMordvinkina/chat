package com.example.chat.repositories;

import com.example.chat.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long>, CustomChatRepository {
    @Query("SELECT c " +
            "FROM ChatEntity c " +
            "JOIN c.participants pr " +
            "JOIN pr.key.user p " +
            "WHERE c.id=:chatId AND p.user.id=:userId " +
            "ORDER BY pr.type")
    ChatEntity getChatByIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

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
    ChatEntity getChat(@Param("userIdOne") Long userIdOne, @Param("userIdTwo") Long userIdTwo);
}
