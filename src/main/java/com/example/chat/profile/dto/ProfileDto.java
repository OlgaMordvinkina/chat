package com.example.chat.profile.dto;

import com.example.chat.setting.dto.SettingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private Long id;
    private String name;
    private String surname;
    private String photo;
    private SettingDto setting;
    private Long userId;
}
