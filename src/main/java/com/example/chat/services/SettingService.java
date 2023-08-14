package com.example.chat.services;

import com.example.chat.dto.SettingDto;

public interface SettingService {
    SettingDto getSettingById(Long settingId);
}
