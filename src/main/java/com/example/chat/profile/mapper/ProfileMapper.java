package com.example.chat.profile.mapper;

import com.example.chat.profile.dto.ProfileDto;
import com.example.chat.profile.entity.ProfileEntity;
import com.example.chat.user.dto.UserRegisterDto;
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
