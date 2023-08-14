package com.example.chat.mappers;

import com.example.chat.dto.ChatDto;
import com.example.chat.entities.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(target = "title", source = "title")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "participants", ignore = true)
    ChatDto toChatDto(ChatEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "participants", ignore = true)
    ChatEntity toChatEntity(ChatDto dto);
}
