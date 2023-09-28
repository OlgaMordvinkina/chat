package com.example.chat.mappers;

import com.example.chat.dto.AttachmentDto;
import com.example.chat.entities.AttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    @Mapping(target = "file", source = "nameFile")
    AttachmentDto toAttachmentDto(AttachmentEntity entity);

    @Mapping(target = "nameFile", source = "file")
    AttachmentEntity toAttachmentEntity(AttachmentDto dto);
}
