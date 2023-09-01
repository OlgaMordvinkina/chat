package com.example.chat.mappers;

import com.example.chat.dto.AttachmentDto;
import com.example.chat.entities.AttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDto toAttachmentDto(AttachmentEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    AttachmentEntity toAttachmentEntity(AttachmentDto dto);
}
