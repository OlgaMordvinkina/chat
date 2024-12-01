package org.mediagate.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime onlineDate;
}
