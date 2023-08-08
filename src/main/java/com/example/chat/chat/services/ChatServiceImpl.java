package com.example.chat.chat.services;

import com.example.chat.chat.dto.ChatDto;
import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.chat.mapper.ChatMapper;
import com.example.chat.chat.repositories.ChatRepository;
import com.example.chat.participant.services.ParticipantService;
import com.example.chat.user.dto.UserDto;
import com.example.chat.user.mapper.UserMapper;
import com.example.chat.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepository;
    private final ParticipantService participantService;
    private final ChatRepository repository;
    private final ChatMapper chatMapper;
    private final UserMapper userMapper;

    @Override
    public ChatDto createChat(Long userId, ChatDto chatDto) {
        Set<Long> participantsIds = chatDto.getParticipants().stream()
                .map(UserDto::getId)
                .collect(Collectors.toSet());

        //проверяем существуют ли такие пользователи по списку ИД
        Set<UserDto> users = userRepository.findAllByIdIn(participantsIds).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toSet());

        ChatEntity saveShat = repository.save(chatMapper.toChatEntity(chatDto));

        chatDto.setParticipants(users);

        participantService.addParticipants(userId, saveShat.getId(), participantsIds);
        return chatMapper.toChatDto(saveShat);
    }
}
