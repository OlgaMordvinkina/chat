package com.example.chat.participant.services;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.chat.enums.Availability;
import com.example.chat.exceptions.AccessException;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.participant.entity.CompositeKey;
import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.entity.ParticipantMessageEntity;
import com.example.chat.participant.entity.ParticipantMessageKey;
import com.example.chat.participant.enums.TypeParticipant;
import com.example.chat.participant.repositories.ParticipantMessageRepositories;
import com.example.chat.participant.repositories.ParticipantRepository;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.user.repositories.UserRepository;
import com.example.chat.utils.ValidationsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantMessageRepositories participantMessageRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ValidationsUtils utils;

    @Override
    public List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds) {
        List<ParticipantEntity> newParticipants = new ArrayList<>();

        UserEntity initiator = utils.getUserById(userId);
        ChatEntity chat = utils.getChatById(chatId);

        //если участников нет (т.е. чат только создан) и он групповой, то в чат закладываем роль админа
        var newParticipant = ParticipantEntity.builder();
        if (!ValidationsUtils.checkForNullOrEmpty(chat.getParticipants()) && chat.getType().equals(Availability.GROUP)) {
            newParticipant.type(TypeParticipant.ADMIN);
            newParticipant.key(new CompositeKey(chat, initiator));
            newParticipants.add(newParticipant.build());
        } else if (chat.getType().equals(Availability.GROUP) &&
                !Objects.equals(
                        participantRepository.findByKey_Chat_IdAndType(chatId, TypeParticipant.ADMIN).getKey().getUser().getId(),
                        userId)) { //проверяем явл-ся ли админом инициатор приглашения
            throw new AccessException("No rights to edit chat");
        }

        List<Long> participantsIdsSaved = ValidationsUtils.checkForNullOrEmpty(chat.getParticipants()) ?
                chat.getParticipants().stream()
                        .map(it -> it.getKey().getUser().getId())
                        .toList() :
                null;

        List<UserEntity> users = userRepository.findAllByIdIn(participantsIds);

        //отсеивание запросов на приглашение уже имеющихся участников
        if (ValidationsUtils.checkForNullOrEmpty(participantsIdsSaved)) {
            users.removeIf(it -> participantsIdsSaved.contains(it.getId()));
        }


        users.forEach(it -> {
            newParticipant.type(TypeParticipant.REGULAR);
            newParticipant.key(new CompositeKey(chat, it));
            newParticipants.add(newParticipant.build());
        });

        return participantRepository.saveAll(newParticipants);
    }

    /**
     * Удалять участника имеет право только админ (В GROUP-chat) и пользователь сам себя (GROUP- и PRIVATE- chat).
     * Если это PRIVATE-чат, то когда пользователь-1 удаляет чат, то для него удаляется чат и смс.
     * Если удаляется последний участник, то смс и чат удаляются без возможности восстановления.
     */
    @Override
    public void deleteParticipantById(Long userId, Long chatId, Long deletedUserId) {
        utils.existUserById(userId);
        UserEntity user = utils.getUserById(deletedUserId);
        ChatEntity chat = utils.getChatById(chatId);

        if ((chat.getParticipants().stream().noneMatch(participant -> participant.getKey().getUser().getId().equals(userId))
                && !userId.equals(deletedUserId))
                || (!chat.getType().equals(Availability.GROUP) &&
                !participantRepository.findByKey_Chat_IdAndType(chatId, TypeParticipant.ADMIN)
                        .getKey().getUser().getId().equals(userId))
        ) {
            throw new AccessException("No rights to delete to this user");
        }
        participantMessageRepository.deleteAllByKey_Chat_IdAndKey_User_Id(chatId, userId);
        participantRepository.deleteByKey_Chat_IdAndKey_User_Id(chatId, deletedUserId);
    }

    @Override
    public ParticipantMessageEntity addMessagesToParticipant(UserEntity user, ChatEntity chat, MessageEntity message) {
        ParticipantMessageKey key = new ParticipantMessageKey(chat, user, message);
        ParticipantMessageEntity participantMessage = new ParticipantMessageEntity(key);
        return participantMessageRepository.save(participantMessage);
    }

    @Override
    public void deleteMessagesToParticipantByChatId(Long chatId) {
        participantMessageRepository.deleteAllByKey_Chat_Id(chatId);
    }

    @Override
    public void deleteMessagesToParticipantByMessageId(Long messageId) {
        participantMessageRepository.deleteAllByKey_Message_Id(messageId);
    }
}
