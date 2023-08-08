package com.example.chat.participant.entity;

import com.example.chat.chat.entity.ChatEntity;
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
public class CompositeKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private ChatEntity chat;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private UserEntity user;
}
