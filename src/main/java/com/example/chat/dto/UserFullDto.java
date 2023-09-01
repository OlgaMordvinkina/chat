package com.example.chat.dto;

import com.example.chat.dto.enums.TypeParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullDto {
    private Long id;
    private String fullName;
    private String email;
    private String lastEntryDate;
    private TypeParticipant type;
}
