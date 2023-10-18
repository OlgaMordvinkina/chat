package com.example.chat.entities;

import com.example.chat.dto.enums.StateMessage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private ProfileEntity sender;
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatEntity chat;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Enumerated(value = EnumType.STRING)
    private StateMessage state;
    @Column(columnDefinition = "TEXT")
    private String text;

//    @Column todo
//    private Long privateMessageId;

    @ManyToOne
    @JoinColumn(name = "reply_message_id", unique = false)
//    @JoinColumn(name = "reply_message", unique = false)
    private MessageEntity replyMessage;

    @OneToMany
    @JoinColumn(name = "forward_from")
    private List<MessageEntity> forwardFrom;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private List<AttachmentEntity> attachments;
}
