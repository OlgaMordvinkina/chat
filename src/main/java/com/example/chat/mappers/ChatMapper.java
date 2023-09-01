package com.example.chat.mappers;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.entities.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ChatMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "participants", ignore = true)
    ChatDto toChatDto(ChatEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "participants", ignore = true)
    ChatEntity toChatEntity(ChatDto dto);

    @Mapping(target = "participants", expression = "java((java.util.List<com.example.chat.dto.UserFullDto>) entity.getParticipants().stream()" +
            ".map(INSTANCE::toUserFullDto).collect(java.util.stream.Collectors.toList()))")
    ChatFullDto toChatFullDto(ChatEntity entity);
}
