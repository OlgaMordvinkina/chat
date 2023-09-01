package com.example.chat.dto;

import com.example.chat.dto.enums.StateMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    //    private Long senderId;
    private ProfileDto sender;
    //    private Long chatId;
    private ChatDto chat;
    private LocalDateTime createDate;
    private StateMessage state;
    @NotBlank
    private String text;
    private Long replyMessageId;
    private Set<Long> forwardFromIds;
    private List<AttachmentDto> attachments;
}
