package com.example.chat.mappers;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.ParticipantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ChatMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "participants", source = "entity.participants", qualifiedByName = "participantsIds")
    ChatDto toChatDto(ChatEntity entity);

    @Named("participantsIds")
    default Set<Long> getParticipantsIds(Set<ParticipantEntity> participants) {
        return participants != null ?
                participants.stream()
                        .map(it -> it.getKey().getUser().getId())
                        .collect(Collectors.toSet()) :
                Set.of();
    }

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "participants", ignore = true)
    ChatEntity toChatEntity(ChatDto dto);

    @Mapping(target = "participants", source = "entity.participants", qualifiedByName = "participants")
    @Mapping(target = "attachments", ignore = true)
    ChatFullDto toChatFullDto(ChatEntity entity);

    @Named("participants")
    default List<UserFullDto> getParticipants(Set<ParticipantEntity> participants) {
        return participants != null ?
                participants.stream()
                        .map(INSTANCE::toUserFullDto)
                        .collect(java.util.stream.Collectors.toList()) :
                List.of();
    }
}
