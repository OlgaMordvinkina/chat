package com.example.chat.dto;

import com.example.chat.dto.enums.TypeParticipant;
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
