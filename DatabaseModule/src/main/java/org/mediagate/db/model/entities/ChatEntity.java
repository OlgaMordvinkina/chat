package org.mediagate.db.model.entities;

import lombok.experimental.SuperBuilder;
import org.mediagate.db.enums.Availability;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Chats")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChatEntity extends ABaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
    String title;
    String photo;

    @Column(name = "chat_type")
    @Enumerated(value = EnumType.STRING)
    Availability type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    @ToString.Exclude
    Set<ParticipantEntity> participants;
}
