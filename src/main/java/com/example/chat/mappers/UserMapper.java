package com.example.chat.mappers;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.ParticipantEntity;
import com.example.chat.entities.PasswordEntity;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    @Mapping(target = "id", source = "participant.key.user.id")
    @Mapping(target = "fullName", source = "participant.key.user", qualifiedByName = "fullName")
    @Mapping(target = "email", source = "participant.key.user.user.email")
    @Mapping(target = "lastEntryDate", expression = "java(null)")
    @Mapping(target = "type", source = "participant.type")
    UserFullDto toUserFullDto(ParticipantEntity participant);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "profile", qualifiedByName = "fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "lastEntryDate", expression = "java(null)")
    @Mapping(target = "type", expression = "java(null)")
    UserFullDto toUserFullDto(ProfileEntity profile);

    @Named("fullName")
    default String getFullName(ProfileEntity profile) {
        return profile.getSurname() + " " + profile.getName();
    }
}
