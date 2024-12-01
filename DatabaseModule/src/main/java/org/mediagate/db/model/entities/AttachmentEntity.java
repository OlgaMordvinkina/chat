package org.mediagate.db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Attachments")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AttachmentEntity extends ABaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
    @Column(name = "message_id")
    Long messageId;
    @Column(name = "name_file")
    String nameFile;
}
