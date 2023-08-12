package com.example.chat.chat.repositories;

import com.example.chat.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long>, ChatCustomRepository {

//    @Query(value = "select new ChatPreviewDto(ChatEntity.id, ChatEntity.title, m.text, m.createDate, m.state) " +
//            "from ChatEntity as chat" +
//            "inner join MessageEntity AS m on m.chat.id = ChatEntity.id " +
//            "inner join ParticipantEntity as p on p.key.chat.id = ChatEntity.id " +
//            "where p.key.user.id = :userId and m.createDate=(select max(msg.createDate) from MessageEntity as msg) " +
//            "group by ChatEntity.id, ChatEntity.title, m.text, m.createDate, m.state", nativeQuery = true)

//    @Query(value = "SELECT c.id, c.title, m.\"text\", m.create_date, m.state " +
//            "FROM chats c inner join messages m ON m.chat_id = c.id " +
//            "INNER join participants p ON p.chat_id = c.id " +
//            "where p.user_id=? and m.create_date=(select max(msg.create_date) from messages msg) " +
//            "group by c.id, c.title, m.\"text\", m.create_date, m.state", nativeQuery = true)
//    Object[] get(Long userId);
}
