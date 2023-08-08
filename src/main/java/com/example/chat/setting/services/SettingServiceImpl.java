package com.example.chat.setting.services;

import com.example.chat.setting.dto.SettingDto;
import com.example.chat.setting.entity.SettingEntity;
import com.example.chat.setting.mapper.SettingMapper;
import com.example.chat.setting.repositories.SettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    @Override
    public SettingDto getSettingById(Long settingId) {
        SettingEntity settingDefault = settingRepository.findById(settingId).orElse(null);
        return settingMapper.toSettingDto(settingDefault);
    }
}
