package com.example.chat.repositories;

import com.example.chat.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByChat_IdOrderById(Long chatId);

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
}
