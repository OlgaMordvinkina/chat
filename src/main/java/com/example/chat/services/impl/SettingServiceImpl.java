package com.example.chat.services.impl;

import com.example.chat.dto.SettingDto;
import com.example.chat.entities.SettingEntity;
import com.example.chat.mappers.SettingMapper;
import com.example.chat.repositories.SettingRepository;
import com.example.chat.services.SettingService;
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

    @Override
    @Transactional
    public SettingEntity createSetting(SettingEntity entity) {
        return settingRepository.save(entity);
    }
}
