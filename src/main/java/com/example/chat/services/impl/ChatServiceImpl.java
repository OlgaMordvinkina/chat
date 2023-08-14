package com.example.chat.services.impl;

import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.dto.UserDto;
import com.example.chat.entities.ChatEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.mappers.ChatMapper;
import com.example.chat.mappers.UserMapper;
import com.example.chat.repositories.ChatRepository;
import com.example.chat.repositories.UserRepository;
import com.example.chat.services.ChatService;
import com.example.chat.services.ParticipantService;
import com.example.chat.utils.ValidationsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ValidationsUtils utils;
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final UserMapper userMapper;

    @Override
    public ChatDto createChat(Long userId, ChatDto chat) {
        Set<Long> participantsIds = chat.getParticipants().size() > 0 ?
                chat.getParticipants().stream()
                        .map(UserDto::getId)
                        .collect(Collectors.toSet()) :
                Set.of();

        //проверяем существуют ли такие пользователи по списку ИД
        Set<UserDto> users = userRepository.findAllByIdIn(participantsIds).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toSet());

        ChatEntity saveShat = chatRepository.save(chatMapper.toChatEntity(chat));

        chat.setParticipants(users);

        participantService.addParticipants(userId, saveShat.getId(), participantsIds);
        return chatMapper.toChatDto(saveShat);
    }


    /**
     * Если User-1 удаляет чат, то он будет удалён из участников этого чата.
     * (касается только PRIVATE чатой) Если User-1 снова напишет собеседнику или собеседник напишет User-1,
     * то User-1 опять станет его участником.
     * Чат и его messages будут удалёны только после выхода из него последнего участника.
     */
    public void deleteChat(Long userId, Long chatId) {
        //todo: тут недоделано, потому что надо решить как действовать при удалении

        utils.existUserById(userId);
        ChatEntity chat = utils.getChatById(chatId);

        if (chat.getParticipants().stream().noneMatch(it -> it.getKey().getUser().getId().equals(userId))) {
            throw new AccessException("No rights to delete this chat");
        }

        if (chat.getParticipants().size() > 1) {

        }
    }

    @Override
    public Set<ChatPreviewDto> getChatPreview(Long userId) {
        utils.existUserById(userId);
        return chatRepository.findChatPreview(userId);
    }
}
