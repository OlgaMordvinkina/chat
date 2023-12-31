package com.example.chat.dto;

import com.example.chat.dto.enums.StateMessage;
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
    private ProfileDto sender;
    private ChatDto chat;
    private LocalDateTime createDate;
    private StateMessage state;
    private String text;
    private MessageDto replyMessage;
    private List<MessageDto> forwardedFrom;
    private List<AttachmentDto> attachments;

    private String typeMessage;
}
