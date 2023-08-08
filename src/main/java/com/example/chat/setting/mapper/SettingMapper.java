package com.example.chat.setting.mapper;

import com.example.chat.setting.dto.SettingDto;
import com.example.chat.setting.entity.SettingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    SettingDto toSettingDto(SettingEntity entity);

    SettingEntity toSettingEntity(SettingDto dto);
}
