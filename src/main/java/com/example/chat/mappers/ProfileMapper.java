package com.example.chat.mappers;

import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "userId", source = "user.id")
    ProfileDto toProfileDto(ProfileEntity entity);

    @Mapping(target = "user", ignore = true)
    ProfileEntity toProfileEntity(ProfileDto dto);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "photo", expression = "java(null)")
    @Mapping(target = "setting", expression = "java(null)")
    @Mapping(target = "userId", expression = "java(null)")
    ProfileDto toProfileDto(UserRegisterDto registerDto);
}
