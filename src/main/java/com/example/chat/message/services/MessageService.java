package com.example.chat.message.services;

import com.example.chat.message.dto.MessageDto;

public interface MessageService {
    MessageDto createMessage(MessageDto newMessage);
}
