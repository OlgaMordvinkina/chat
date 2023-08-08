package com.example.chat.message.mapper;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.message.dto.MessageDto;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.user.entity.UserEntity;
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
