package com.example.chat.mappers;

import com.example.chat.dto.MessageDto;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.MessageEntity;
import com.example.chat.entities.ParticipantEntity;
import com.example.chat.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    //    @Mapping(target = "senderId", source = "entity.sender.id")
    @Mapping(target = "sender", source = "entity.sender")
    @Mapping(target = "sender.userId", source = "entity.sender.user.id")
    @Mapping(target = "chat", source = "entity.chat")
    @Mapping(target = "replyMessageId", source = "entity.replyMessage.id")
    @Mapping(target = "forwardFromIds", source = "entity.forwardFrom", qualifiedByName = "forwardFromIds")
    @Mapping(target = "chat.participants", source = "entity.chat.participants", qualifiedByName = "participants")
    MessageDto toMessageDto(MessageEntity entity);

    @Named("forwardFromIds")
    static Set<Long> getForwardFromIds(List<MessageEntity> forwardFrom) {
        return forwardFrom != null
                ? forwardFrom.stream()
                .map(MessageEntity::getId)
                .collect(Collectors.toSet())
                : null;
    }

    @Named("participants")
    static Set<Long> getParticipantsIds(Set<ParticipantEntity> participants) {
        return participants != null
                ? participants.stream()
                .map(participant -> participant.getKey().getUser().getId())
                .collect(Collectors.toSet())
                : null;
    }

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "createDate", source = "dto.createDate")
    @Mapping(target = "state", source = "dto.state")
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "chat", source = "chat")
    @Mapping(target = "replyMessage", source = "replyMessage")
    @Mapping(target = "forwardFrom", source = "forwardFrom")
    @Mapping(target = "attachments", expression = "java(null)")
    MessageEntity toMessageEntity(MessageDto dto, ProfileEntity sender, ChatEntity chat, MessageEntity replyMessage,
                                  List<MessageEntity> forwardFrom);
}
