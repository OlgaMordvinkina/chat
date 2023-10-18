package com.example.chat.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Attachments")
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message_id")
    private Long messageId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "name_file")
    private String nameFile;
}
