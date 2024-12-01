package org.mediagate.core.mappers;

import org.mediagate.core.dto.ProfileDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.db.model.entities.ProfileEntity;
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
    @Mapping(target = "onlineDate", expression = "java(null)")
    ProfileDto toProfileDto(UserRegisterDto registerDto);
}
