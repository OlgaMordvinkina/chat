package com.example.chat.chat.dto;

import com.example.chat.chat.enums.Availability;
import com.example.chat.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatDto {
    private Long id;
    private String title;
    private Availability type;
    private Set<UserDto> participants;
}
