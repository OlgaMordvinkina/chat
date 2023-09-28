package com.example.chat.dto;

import com.example.chat.dto.enums.TypeParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullDto {
    private Long id;
    private String fullName;
    private String email;
    private LocalDateTime onlineDate;
    private TypeParticipant type;
    private String photo;
}
