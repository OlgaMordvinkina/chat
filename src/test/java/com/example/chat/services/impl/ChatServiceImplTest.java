package com.example.chat.services.impl;

import com.example.chat.BaseApplicationTests;
import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.dto.enums.Availability;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.services.ChatService;
import com.example.chat.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceImplTest extends BaseApplicationTests {
    private static UserDto user1 = new UserDto();
    private static UserDto user2 = new UserDto();
    private final UserRegisterDto userRegister1 = new UserRegisterDto(
            "_test@mail.ru",
            "password",
            "password",
            "Name-1",
            "Surname-1",
            null,
            null
    );
    private final UserRegisterDto userRegister2 = new UserRegisterDto(
            "_test@mail.ru",
            "password",
            "password",
            "Name-2",
            "Surname-2",
            null,
            null
    );
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @Test
    void createChat_successfully() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of(user1.getId(), user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        ChatDto expectedChat = new ChatDto(createdChat.getId(), null, null, Availability.PRIVATE, Set.of());

        assertNotNull(createdChat.getId());
        assertEquals(createdChat.getId(), expectedChat.getId());
    }

    @Test
    void createChat_noParticipants() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of());
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        ChatDto expectedChat = new ChatDto(createdChat.getId(), null, null, Availability.PRIVATE, Set.of());

        assertNotNull(createdChat.getId());
        assertEquals(createdChat, expectedChat);
    }

    @Test
    void createChat_private_that_already_exists() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of(user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        ChatDto existsChat = chatService.createChat(user1.getId(), chatDto);

        assertEquals(createdChat.getId(), existsChat.getId());
    }

    @Test
    void deleteChat_successfully() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.GROUP, Set.of(user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        ChatFullDto chatBeforeDelete = chatService.getChat(user1.getId(), createdChat.getId());
        assertNotNull(chatBeforeDelete.getId());

        chatService.deleteChat(user1.getId(), createdChat.getId());
        assertThrows(AccessException.class, () -> chatService.getChat(user1.getId(), createdChat.getId()));
    }

    @Test
    void deleteChat_not_found_user() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.GROUP, Set.of(user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        assertThrows(NotFoundObjectException.class, () -> chatService.deleteChat(999L, createdChat.getId()));
    }

    @Test
    void getChat_successfully() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of(user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        ChatFullDto chat = chatService.getChat(user1.getId(), createdChat.getId());
        assertEquals(createdChat.getId(), chat.getId());
    }

    @Test
    void getChat_not_found_user() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of(user2.getId()));
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        assertThrows(NotFoundObjectException.class, () -> chatService.getChat(999L, createdChat.getId()));
    }

    @Test
    void getChat_no_rights_to_chat() {
        creatingUsers();
        ChatDto chatDto = getChatDto(Availability.PRIVATE, Set.of());
        ChatDto createdChat = chatService.createChat(user1.getId(), chatDto);
        assertThrows(AccessException.class, () -> chatService.getChat(user2.getId(), createdChat.getId()));
    }

    private ChatDto getChatDto(Availability type, Set<Long> participants) {
        ChatDto chatDto = new ChatDto();
        chatDto.setType(type);
        chatDto.setParticipants(participants);
        return chatDto;
    }

    private void creatingUsers() {
        userRegister1.setEmail(getRandomEmail() + userRegister1.getEmail());
        user1 = userService.createUser(userRegister1);
        userRegister2.setEmail(getRandomEmail() + userRegister2.getEmail());
        user2 = userService.createUser(userRegister2);
    }

    private String getRandomEmail() {
        Random random = new Random();
        int length = 8;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('a' + random.nextInt(26));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}