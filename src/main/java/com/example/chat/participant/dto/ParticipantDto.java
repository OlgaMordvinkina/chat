package com.example.chat.participant.dto;

import com.example.chat.message.dto.MessageDto;
import com.example.chat.participant.enums.TypeParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDto {
    private Long chatId;
    private Set<Long> usersIds;
    private TypeParticipant type;
    private List<MessageDto> messages;
}
