package com.example.chat.mappers;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.PasswordEntity;
import com.example.chat.entities.UserEntity;
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
