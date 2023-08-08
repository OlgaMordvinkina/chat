package com.example.chat.chat.entity;

import com.example.chat.chat.enums.Availability;
import com.example.chat.participant.entity.ParticipantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Chats")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(value = EnumType.STRING)
    private Availability type;

    @OneToMany
    @JoinColumn(name = "chat_id")
    private Set<ParticipantEntity> participants;
}
