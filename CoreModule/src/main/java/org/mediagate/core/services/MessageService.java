package org.mediagate.core.services;

import org.mediagate.core.dto.MessageDto;
import org.mediagate.core.dto.enums.TypeSearch;
import org.mediagate.db.model.entities.ChatEntity;

import java.util.List;

public interface MessageService {
    MessageDto createMessage(Long userId, Long chatId, MessageDto newMessage);

    MessageDto updateMessage(MessageDto updateMessage);

    MessageDto deleteMessage(Long userId, Long chatId, Long messageId);

    List<MessageDto> getMessages(Long userId, Long chatId, int page, int size);

    MessageDto getLastMessageByChat(Long chatId);

    List<MessageDto> searchMessagesChats(Long userId, Long chatId, String desired, TypeSearch type) throws IllegalAccessException;

    ChatEntity updateStateMessages(Long userId, Long chatId);
}
