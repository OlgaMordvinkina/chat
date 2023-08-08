package com.example.chat.user.mapper;

import com.example.chat.password.entity.PasswordEntity;
import com.example.chat.user.dto.UserDto;
import com.example.chat.user.dto.UserRegisterDto;
import com.example.chat.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "password", source = "password")
    UserEntity toUserEntity(UserDto dto, PasswordEntity password);

    @Mapping(target = "id", source = "id")
    UserDto toUserDto(UserEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "role", expression = "java(null)")
    UserDto toUserDto(UserRegisterDto registerDto);
}
