package com.example.chat.entities;

import com.example.chat.dto.enums.Availability;
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
    private String photo;

    @Column(name = "chat_type")
    @Enumerated(value = EnumType.STRING)
    private Availability type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @ToString.Exclude
    private Set<ParticipantEntity> participants;
}
