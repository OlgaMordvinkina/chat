package com.example.chat.message.dto;

import com.example.chat.attachment.dto.AttachmentDto;
import com.example.chat.message.enums.StateMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long chatId;
    private LocalDateTime createDate;
    private StateMessage state;
    private String text;
    private String replyMessage;
    private String forwardFrom;
    private List<AttachmentDto> attachments;
}
