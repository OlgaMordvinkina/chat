package com.example.chat.mappers;

import com.example.chat.dto.SettingDto;
import com.example.chat.entities.SettingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingMapper {
    SettingDto toSettingDto(SettingEntity entity);

    SettingEntity toSettingEntity(SettingDto dto);
}
