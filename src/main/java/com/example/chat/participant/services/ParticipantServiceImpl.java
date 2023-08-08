package com.example.chat.participant.services;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.chat.enums.Availability;
import com.example.chat.chat.repositories.ChatRepository;
import com.example.chat.message.exception.AccessException;
import com.example.chat.message.exception.NotFoundChatException;
import com.example.chat.participant.entity.CompositeKey;
import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.enums.TypeParticipant;
import com.example.chat.participant.repositories.ParticipantRepository;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.user.exceptions.NotFoundUserException;
import com.example.chat.user.repositories.UserRepository;
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
    private final ParticipantRepository participantRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds) {
        List<ParticipantEntity> newParticipants = new ArrayList<>();

        UserEntity initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundUserException(userId));
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> new NotFoundChatException(chatId));

        //если участников нет (т.е. чат только создан) и он групповой, то в чат закладываем роль админа
        var newParticipant = ParticipantEntity.builder();
        if (!checkForNullOrEmpty(chat.getParticipants()) && chat.getType().equals(Availability.GROUP)) {
            newParticipant.type(TypeParticipant.ADMIN);
            newParticipant.key(new CompositeKey(chat, initiator));
            newParticipants.add(newParticipant.build());
        } else if (chat.getType().equals(Availability.GROUP) &&
                !Objects.equals(
                        participantRepository.findByKey_Chat_IdAndType(chatId, TypeParticipant.ADMIN).getKey().getUser().getId(),
                        userId)) { //проверяем явл-ся ли админом инициатор приглашения
            throw new AccessException("No rights to edit chat");
        }

        List<Long> participantsIdsSaved = checkForNullOrEmpty(chat.getParticipants()) ?
                chat.getParticipants().stream()
                        .map(it -> it.getKey().getUser().getId())
                        .toList() :
                null;

        List<UserEntity> users = userRepository.findAllByIdIn(participantsIds);

        //отсеивание запросов на приглашение уже имеющихся участников
        if (checkForNullOrEmpty(participantsIdsSaved)) {
            users.removeIf(it -> participantsIdsSaved.contains(it.getId()));
        }


        users.forEach(it -> {
            newParticipant.type(TypeParticipant.REGULAR);
            newParticipant.key(new CompositeKey(chat, it));
            newParticipants.add(newParticipant.build());
        });

        return participantRepository.saveAll(newParticipants);
    }

    private boolean checkForNullOrEmpty(Object obj) {
        if (obj == null) {
            return false;
        } else {
            return !obj.toString().isEmpty();
        }
    }
}
