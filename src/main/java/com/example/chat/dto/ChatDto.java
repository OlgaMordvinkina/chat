package com.example.chat.dto;

import com.example.chat.dto.enums.Availability;
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
    private Set<Long> participants;
//    private Set<UserDto> participants;
}
