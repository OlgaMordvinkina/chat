package com.example.chat.services.impl;

import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.enums.Availability;
import com.example.chat.dto.enums.TypeParticipant;
import com.example.chat.entities.ChatEntity;
import com.example.chat.entities.CompositeKey;
import com.example.chat.entities.ParticipantEntity;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.mappers.UserMapper;
import com.example.chat.repositories.ChatRepository;
import com.example.chat.repositories.ParticipantRepository;
import com.example.chat.repositories.ProfileRepository;
import com.example.chat.services.ParticipantService;
import com.example.chat.services.ProfileService;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ChatRepository chatRepository;
    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final ProfileService profileService;
    private final UserMapper userMapper;

    @Override
    public List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds) {
        List<ParticipantEntity> newParticipants = new ArrayList<>();
        Set<Long> newParticipantsIds = new java.util.HashSet<>(Set.copyOf(participantsIds));

        ProfileEntity initiator = profileService.findProfileByUserId(userId);
        ChatEntity chat = getChatById(chatId);

        //если участников нет (т.е. чат только создан) и он групповой, то в чат закладываем роль админа
        var newParticipant = ParticipantEntity.builder();
        boolean isGroup = chat.getType().equals(Availability.GROUP);
        if (isNullOrEmpty(chat.getParticipants()) && isGroup) {
            newParticipant.type(TypeParticipant.ADMIN);
            newParticipant.key(new CompositeKey(chatId, initiator));
            newParticipants.add(newParticipant.build());
        } else {
            boolean isAdmin = !isGroup && chat.getParticipants() != null && chat.getParticipants().stream()
                    .filter(participant -> participant.getKey().getUser().getId().equals(userId))
                    .anyMatch(participant -> participant.getType().equals(TypeParticipant.ADMIN));
            if (isAdmin) { //проверяем явл-ся ли админом инициатор приглашения
                throw new AccessException("No rights to edit chat");
            }
            newParticipantsIds.add(userId);
        }

        List<Long> participantsIdsSaved = !isNullOrEmpty(chat.getParticipants()) ?
                chat.getParticipants().stream()
                        .map(it -> it.getKey().getUser().getId())
                        .toList() :
                null;

        List<ProfileEntity> users = profileRepository.findAllByUserIdIn(newParticipantsIds);

        //отсеивание запросов на приглашение уже имеющихся участников
        if (!isNullOrEmpty(participantsIdsSaved)) {
            users.removeIf(it -> participantsIdsSaved.contains(it.getId()));
        }

        users.forEach(user -> {
            newParticipant.type(TypeParticipant.REGULAR);
            newParticipant.key(new CompositeKey(chatId, user));
            newParticipants.add(newParticipant.build());
        });

        return participantRepository.saveAll(newParticipants);
    }

    @Override
    public UserFullDto addParticipant(Long userId, Long chatId, Long participantUserId) {
        userService.existUserById(userId);
        getChatById(chatId);
        userService.existUserById(participantUserId);

        ProfileEntity profile = profileRepository.findByUserId(participantUserId);
        var participant = ParticipantEntity.builder();
        participant.key(new CompositeKey(chatId, profile));
        participant.type(TypeParticipant.REGULAR);
        return userMapper.toUserFullDto(participantRepository.save(participant.build()));
    }

    /**
     * Удалять участника имеет право только ADMIN (в GROUP-chat), смс и чат удаляются без возможности восстановления.
     * У всех участников беседы.
     */
    @Override
    public void deleteParticipantById(Long userId, Long chatId, Long deletedUserId) {
        userService.existUserById(userId);
        userService.existUserById(deletedUserId);
        ChatEntity chat = getChatById(chatId);

        boolean isParticipant = chat.getParticipants().stream().anyMatch(participant -> participant.getKey().getUser().getId().equals(userId));
        boolean isRightToDelete = userId.equals(deletedUserId);
        boolean isGroupChat = chat.getType().equals(Availability.GROUP);
        boolean isAdminChat = chat.getParticipants().stream()
                .filter(participant -> participant.getKey().getUser().getId().equals(userId))
                .anyMatch(participant -> participant.getType().equals(TypeParticipant.ADMIN));

        if (chat.getParticipants().size() > 0 && (!isParticipant && !isRightToDelete) || (!isGroupChat && !isAdminChat)) {
            throw new AccessException("No rights to delete to this user");
        }
        participantRepository.deleteByKey_ChatIdAndKey_User_Id(chatId, deletedUserId);
    }

    @Transactional(readOnly = true)
    public ChatEntity getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() -> new NotFoundObjectException("Chat with ID=" + chatId + " does not exist."));
    }

    private boolean isNullOrEmpty(Object obj) {
        return obj == null || obj.toString().isEmpty();
    }
}
