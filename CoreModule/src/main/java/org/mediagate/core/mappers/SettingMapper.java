package org.mediagate.core.mappers;

import org.mediagate.core.dto.SettingDto;
import org.mediagate.db.model.entities.SettingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    SettingDto toSettingDto(SettingEntity entity);

    SettingEntity toSettingEntity(SettingDto dto);
}
