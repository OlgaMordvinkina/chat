package com.example.chat.dto;

import com.example.chat.dto.enums.StateMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatPreviewDto {
    private Long chatId;
    //    private AttachmentDto attachment;
    private String title;
    private Long senderId;
    private String lastMessage;
    private String dateLastMessage;
    private StateMessage stateMessage;
}
