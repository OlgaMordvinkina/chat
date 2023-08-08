package com.example.chat.participant.entity;

import com.example.chat.participant.enums.TypeParticipant;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Participants")
public class ParticipantEntity {
    @EmbeddedId
    private CompositeKey key;
    @Enumerated(value = EnumType.STRING)
    private TypeParticipant type;
}
