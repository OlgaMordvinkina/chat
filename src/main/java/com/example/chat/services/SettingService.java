package com.example.chat.services;

import com.example.chat.dto.SettingDto;
import com.example.chat.entities.SettingEntity;
import org.springframework.transaction.annotation.Transactional;

public interface SettingService {
    SettingDto getSettingById(Long settingId);

    @Transactional
    SettingEntity createSetting(SettingEntity entity);
}
