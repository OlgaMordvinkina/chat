package com.example.chat.attachment.mapper;

import com.example.chat.attachment.dto.AttachmentDto;
import com.example.chat.attachment.entity.AttachmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDto toAttachmentDto(AttachmentEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    AttachmentEntity toAttachmentEntity(AttachmentDto dto);
}
