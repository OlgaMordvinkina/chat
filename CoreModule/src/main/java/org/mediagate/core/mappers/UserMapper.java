package org.mediagate.core.mappers;

import org.mediagate.core.dto.UserDto;
import org.mediagate.core.dto.UserFullDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.db.model.entities.ParticipantEntity;
import org.mediagate.db.model.entities.PasswordEntity;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.UserEntity;
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
    @Mapping(target = "onlineDate", source = "participant.key.user.onlineDate")
    @Mapping(target = "type", source = "participant.type")
    @Mapping(target = "photo", source = "participant.key.user.photo")
    UserFullDto toUserFullDto(ParticipantEntity participant);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "profile", qualifiedByName = "fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "type", expression = "java(null)")
    @Mapping(target = "photo", source = "photo")
    UserFullDto toUserFullDto(ProfileEntity profile);

    @Named("fullName")
    default String getFullName(ProfileEntity profile) {
        return profile.getSurname() + " " + profile.getName();
    }
}
