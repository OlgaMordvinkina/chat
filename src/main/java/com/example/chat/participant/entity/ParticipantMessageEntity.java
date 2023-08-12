package com.example.chat.participant.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Participant_message")
public class ParticipantMessageEntity {
    @EmbeddedId
    ParticipantMessageKey key;
}
