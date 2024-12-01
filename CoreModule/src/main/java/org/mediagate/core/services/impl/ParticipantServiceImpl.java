package org.mediagate.core.services.impl;

import org.mediagate.core.dto.UserFullDto;
import org.mediagate.db.model.entities.ChatEntity;
import org.mediagate.db.model.entities.CompositeKey;
import org.mediagate.db.model.entities.ParticipantEntity;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.enums.Availability;
import org.mediagate.db.enums.TypeParticipant;

import org.mediagate.core.exceptions.AccessException;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.mediagate.core.mappers.UserMapper;

import org.mediagate.core.services.ParticipantService;
import org.mediagate.core.services.ProfileService;
import org.mediagate.core.services.UserService;
import org.mediagate.db.repositories.ChatRepository;
import org.mediagate.db.repositories.ParticipantRepository;
import org.mediagate.db.repositories.ProfileRepository;
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
        ParticipantEntity newParticipant = new ParticipantEntity();
        boolean isGroup = chat.getType().equals(Availability.GROUP);
        if (isNullOrEmpty(chat.getParticipants()) && isGroup) {
            newParticipant.setType(TypeParticipant.ADMIN);
            newParticipant.setKey(new CompositeKey(chatId, initiator));
            newParticipants.add(newParticipant);
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
            newParticipant.setType(TypeParticipant.REGULAR);
            newParticipant.setKey(new CompositeKey(chatId, user));
            newParticipants.add(newParticipant);
        });

        return participantRepository.saveAll(newParticipants);
    }

    @Override
    public UserFullDto addParticipant(Long userId, Long chatId, Long participantUserId) {
        userService.existUserById(userId);
        getChatById(chatId);
        userService.existUserById(participantUserId);

        ProfileEntity profile = profileRepository.findByUserId(participantUserId);
        ParticipantEntity participant = new ParticipantEntity();
        participant.setKey(new CompositeKey(chatId, profile));
        participant.setType(TypeParticipant.REGULAR);
        return userMapper.toUserFullDto(participantRepository.save(participant));
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
