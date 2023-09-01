package com.example.chat.dto;

import com.example.chat.dto.enums.Availability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatFullDto {
    private Long id;
    private String title;
    private Availability type;
    private List<UserFullDto> participants;
}
