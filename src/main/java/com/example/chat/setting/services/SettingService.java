package com.example.chat.setting.services;

import com.example.chat.setting.dto.SettingDto;

public interface SettingService {
    SettingDto getSettingById(Long settingId);
}
