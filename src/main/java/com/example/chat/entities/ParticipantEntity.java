package com.example.chat.entities;

import com.example.chat.dto.enums.TypeParticipant;
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
