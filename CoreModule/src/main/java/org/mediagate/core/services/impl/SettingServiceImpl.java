package org.mediagate.core.services.impl;

import org.mediagate.core.dto.SettingDto;
import org.mediagate.db.model.entities.SettingEntity;
import org.mediagate.core.mappers.SettingMapper;
import org.mediagate.db.repositories.SettingRepository;
import org.mediagate.core.services.SettingService;
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
