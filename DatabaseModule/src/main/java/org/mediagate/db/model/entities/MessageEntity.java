package org.mediagate.db.model.entities;

import lombok.experimental.SuperBuilder;
import org.mediagate.db.enums.StateMessage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Messages")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MessageEntity extends ABaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    ProfileEntity sender;
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    ChatEntity chat;
    @Column(name = "create_date")
    LocalDateTime createDate;
    @Enumerated(value = EnumType.STRING)
    StateMessage state;
    @Column(name = "text_message", columnDefinition = "TEXT")
    String text;

    @ManyToOne
    @JoinColumn(name = "reply_message_id", unique = false)
    MessageEntity replyMessage;

    @OneToMany
    @ElementCollection
    @CollectionTable(name = "ForwardedMessages", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "forwarded_message_id")
    @ToString.Exclude
    List<MessageEntity> forwardedFrom;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @ToString.Exclude
    List<AttachmentEntity> attachments;
}
