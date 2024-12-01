package org.mediagate.core.mappers;

import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.db.model.entities.AttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    @Mapping(target = "file", source = "entity.nameFile")
    AttachmentDto toAttachmentDto(AttachmentEntity entity);

    @Mapping(target = "nameFile", source = "dto.file")
    @Mapping(target = "messageId", ignore = true)
    AttachmentEntity toAttachmentEntity(AttachmentDto dto);
}
