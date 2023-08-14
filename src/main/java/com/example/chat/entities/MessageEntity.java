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
    private UserEntity sender;
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatEntity chat;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Enumerated(value = EnumType.STRING)
    private StateMessage state;
    private String text;
    @Column(name = "reply_message")
    private String replyMessage;
    @Column(name = "forward_from")
    private String forwardFrom;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private List<AttachmentEntity> attachments;
}
