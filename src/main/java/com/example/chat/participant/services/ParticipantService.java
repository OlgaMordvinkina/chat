package com.example.chat.participant.services;

import com.example.chat.chat.entity.ChatEntity;
import com.example.chat.message.entity.MessageEntity;
import com.example.chat.participant.entity.ParticipantEntity;
import com.example.chat.participant.entity.ParticipantMessageEntity;
import com.example.chat.user.entity.UserEntity;

import java.util.List;
import java.util.Set;

public interface ParticipantService {
    List<ParticipantEntity> addParticipants(Long userId, Long chatId, Set<Long> participantsIds);

    void deleteParticipantById(Long userId, Long chatId, Long deletedUserId);

    ParticipantMessageEntity addMessagesToParticipant(UserEntity user, ChatEntity chat, MessageEntity message);

    void deleteMessagesToParticipantByChatId(Long chatId);

    void deleteMessagesToParticipantByMessageId(Long messageId);
}
