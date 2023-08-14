package com.example.chat.mappers;

import com.example.chat.dto.MessageDto;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.MessageEntity;
import com.example.chat.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "senderId", source = "entity.sender.id")
    @Mapping(target = "chatId", source = "entity.chat.id")
    MessageDto toMessageDto(MessageEntity entity);

    @Mapping(target = "id", expression = "java(null)")
    MessageEntity toMessageEntity(MessageDto dto, UserEntity sender, ChatEntity chat);
}
