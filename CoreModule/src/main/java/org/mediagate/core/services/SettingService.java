package org.mediagate.core.services;

import org.mediagate.core.dto.SettingDto;
import org.mediagate.db.model.entities.SettingEntity;

public interface SettingService {
    SettingDto getSettingById(Long settingId);

    SettingEntity createSetting(SettingEntity entity);
}
