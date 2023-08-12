package com.example.chat.participant.entity;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.user.entity.UserEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ParticipantMessageKey implements Serializable {
    //    @Column(name = "chat_id")
//    private Long chatId;
//    @Column(name = "user_id")
//    private Long userId;
//    @Column(name = "message_id")
//    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private ChatEntity chat;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private MessageEntity message;
}
