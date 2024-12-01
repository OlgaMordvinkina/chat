package org.mediagate.core.mappers;

import org.mediagate.core.dto.AttachmentDto;
import org.mediagate.core.dto.MessageDto;
import org.mediagate.db.model.entities.ChatEntity;
import org.mediagate.db.model.entities.MessageEntity;
import org.mediagate.db.model.entities.ParticipantEntity;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Named("participants")
    static Set<Long> getParticipantsIds(Set<ParticipantEntity> participants) {
        return participants != null
                ? participants.stream()
                .map(participant -> participant.getKey().getUser().getId())
                .collect(Collectors.toSet())
                : null;
    }

    @Mapping(target = "sender", source = "entity.sender")
    @Mapping(target = "sender.userId", source = "entity.sender.user.id")
    @Mapping(target = "chat", source = "entity.chat")
    @Mapping(target = "replyMessage", expression = "java(toMessageDto(entity.getReplyMessage(), null))")
    @Mapping(target = "forwardedFrom", source = "entity.forwardedFrom", qualifiedByName = "forwardedFrom")
    @Mapping(target = "chat.participants", source = "entity.chat.participants", qualifiedByName = "participants")
    @Mapping(target = "typeMessage", expression = "java(null)")
    @Mapping(target = "attachments", source = "attachments")
    MessageDto toMessageDto(MessageEntity entity, List<AttachmentDto> attachments);

    @Mapping(target = "sender", source = "entity.sender")
    @Mapping(target = "sender.userId", source = "entity.sender.user.id")
    @Mapping(target = "chat", source = "entity.chat")
    @Mapping(target = "replyMessage", expression = "java(toMessageDto(entity.getReplyMessage(), null))")
    @Mapping(target = "forwardedFrom", source = "entity.forwardedFrom", qualifiedByName = "forwardedFrom")
    @Mapping(target = "chat.participants", source = "entity.chat.participants", qualifiedByName = "participants")
    @Mapping(target = "typeMessage", expression = "java(null)")
    MessageDto toMessageDto(MessageEntity entity);

    @Named("forwardedFrom")
    default List<MessageDto> getForwardedFrom(List<MessageEntity> forwardedFrom) {
        return forwardedFrom != null
                ? forwardedFrom.stream().map(message -> toMessageDto(message, null)).collect(Collectors.toList())
                : null;
    }

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "createDate", source = "dto.createDate")
    @Mapping(target = "state", source = "dto.state")
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "chat", source = "chat")
    @Mapping(target = "replyMessage", source = "replyMessage")
    @Mapping(target = "forwardedFrom", source = "forwardedFrom")
    @Mapping(target = "attachments", expression = "java(null)")
    MessageEntity toMessageEntity(MessageDto dto, ProfileEntity sender, ChatEntity chat, MessageEntity replyMessage,
                                  List<MessageEntity> forwardedFrom);
}
