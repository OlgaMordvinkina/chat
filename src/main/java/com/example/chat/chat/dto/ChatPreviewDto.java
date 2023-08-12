package com.example.chat.chat.dto;

import com.example.chat.message.enums.StateMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatPreviewDto {
    private Long chatId;
    //    private AttachmentDto attachment;
    private String title;
    private String lastMessage;
    private LocalDateTime dateLastMessage;
    private StateMessage stateMessage;
}
