package org.mediagate.db.model.entities;

import org.mediagate.db.enums.TypeParticipant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
@ToString
@Entity
@Table(name = "Participants")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ParticipantEntity {
    @EmbeddedId
    CompositeKey key;
    @Column(name = "participant_type")
    @Enumerated(value = EnumType.STRING)
    TypeParticipant type;
}
