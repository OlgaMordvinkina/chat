package com.example.chat.services.impl;

import com.example.chat.BaseApplicationTests;
import com.example.chat.dto.*;
import com.example.chat.dto.enums.Availability;
import com.example.chat.dto.enums.StateMessage;
import com.example.chat.dto.enums.TypeSearch;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.repositories.MessageRepository;
import com.example.chat.services.ChatService;
import com.example.chat.services.MessageService;
import com.example.chat.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest extends BaseApplicationTests {
    private static final ChatDto chat = new ChatDto(null, null, null, Availability.GROUP, Set.of());
    private static UserDto user1 = new UserDto();
    private static UserDto user2 = new UserDto();
    private final MessageDto message = new MessageDto(
            null,
            new ProfileDto(),
            new ChatDto(),
            null,
            null,
            "text message",
            null,
            null,
            null,
            null
    );
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
    private MessageService messageService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageRepository messageRepository;

    @Test
    void createMessage_successfully() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertEquals(saveMessage.getText(), message.getText());
    }

    @Test
    void createMessage_no_rights() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        assertThrows(AccessException.class, () -> messageService.createMessage(user2.getId(), saveChat.getId(), message));
    }

    @Test
    void createMessage_NotFoundObjectException_user() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        assertThrows(NotFoundObjectException.class, () -> messageService.createMessage(999L, saveChat.getId(), message));
    }

    @Test
    void createMessage_NotFoundObjectException_chat() {
        creatingUsers();
        assertThrows(NotFoundObjectException.class, () -> messageService.createMessage(user1.getId(), 999L, message));
    }

    @Test
    void updateMessage_successfully() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertEquals(saveMessage.getText(), message.getText());

        String updateText = "update text";
        saveMessage.setText(updateText);
        MessageDto updatedMessage = messageService.updateMessage(saveMessage);

        assertEquals(updateText, updatedMessage.getText());
    }

    @Test
    void updateMessage_state_to_READ() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertEquals(saveMessage.getState(), StateMessage.SENT);

        saveMessage.setState(StateMessage.READ);
        MessageDto updatedMessage = messageService.updateMessage(saveMessage);

        assertEquals(updatedMessage.getState(), StateMessage.READ);
    }

    @Test
    void deleteMessage_successfully() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertNotNull(messageRepository.findById(saveMessage.getId()).orElse(null));

        messageService.deleteMessage(saveMessage.getSender().getUserId(), saveMessage.getChat().getId(), saveMessage.getId());

        assertNull(messageRepository.findById(saveMessage.getId()).orElse(null));
    }

    @Test
    void deleteMessage_NotFoundObjectException_user() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(AccessException.class, () -> messageService.deleteMessage(
                999L,
                saveMessage.getChat().getId(),
                saveMessage.getId()));
    }

    @Test
    void deleteMessage_NotFoundObjectException_chat() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(NotFoundObjectException.class, () -> messageService.deleteMessage(
                saveMessage.getSender().getUserId(),
                999L,
                saveMessage.getId()));
    }

    @Test
    void deleteMessage_NotFoundObjectException_message() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(NotFoundObjectException.class, () -> messageService.deleteMessage(
                saveMessage.getSender().getUserId(),
                saveMessage.getChat().getId(),
                999L));
    }

    @Test
    void getMessages_successfully() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage1 = messageService.createMessage(user1.getId(), saveChat.getId(), message);
        MessageDto saveMessage2 = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        List<MessageDto> messages1 = messageService.getMessages(
                saveMessage1.getSender().getUserId(),
                saveMessage1.getChat().getId(),
                1,
                1);
        assertEquals(1, messages1.size());

        List<MessageDto> messages2 = messageService.getMessages(
                saveMessage1.getSender().getUserId(),
                saveMessage1.getChat().getId(),
                1,
                2);
        assertEquals(2, messages2.size());
    }

    @Test
    void getMessages_NotFoundObjectException_user() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(NotFoundObjectException.class, () -> messageService.getMessages(
                999L,
                saveMessage.getChat().getId(),
                1,
                1));
    }

    @Test
    void getMessages_NotFoundObjectException_chat() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        MessageDto saveMessage = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(NotFoundObjectException.class, () -> messageService.getMessages(
                saveMessage.getSender().getUserId(),
                999L,
                1,
                1));
    }

    @Test
    void getLastMessageByChat_successfully() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);

        MessageDto saveMessage1 = messageService.createMessage(user1.getId(), saveChat.getId(), message);
        MessageDto lastMessage1 = messageService.getLastMessageByChat(saveMessage1.getChat().getId());
        assertEquals(saveMessage1.getId(), lastMessage1.getId());

        MessageDto saveMessage2 = messageService.createMessage(user1.getId(), saveChat.getId(), message);
        MessageDto lastMessage2 = messageService.getLastMessageByChat(saveMessage2.getChat().getId());
        assertEquals(saveMessage2.getId(), lastMessage2.getId());
    }

    @Test
    void searchMessagesChats_this_chat() throws IllegalAccessException {
        creatingUsers();
        message.getSender().setId(user1.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        message.setText("one message");
        MessageDto saveMessage1 = messageService.createMessage(user1.getId(), saveChat.getId(), message);
        message.setText("two message");
        MessageDto saveMessage2 = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        List<MessageDto> messages = messageService.searchMessagesChats(
                user1.getId(),
                saveChat.getId(),
                "two",
                TypeSearch.THIS_CHAT
        );
        assertEquals(saveMessage2.getId(), messages.stream().findFirst().get().getId());
    }

    @Test
    void searchMessagesChats_all_chats() throws IllegalAccessException {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto chat1 = chatService.createChat(user1.getId(), chat);
        message.setText("first message");
        MessageDto saveMessage1 = messageService.createMessage(user1.getId(), chat1.getId(), message);

        ChatDto chat2 = chatService.createChat(user2.getId(), chat);
        message.setText("second message");
        MessageDto saveMessage2 = messageService.createMessage(user2.getId(), chat2.getId(), message);

        List<MessageDto> messages = messageService.searchMessagesChats(
                user2.getId(),
                null,
                "second",
                TypeSearch.ALL_CHATS
        );
        assertEquals(saveMessage2.getId(), messages.stream().findFirst().get().getId());
    }

    @Test
    void searchMessagesChats_IllegalAccessException() {
        creatingUsers();
        message.getSender().setId(user2.getId());
        ChatDto saveChat = chatService.createChat(user1.getId(), chat);
        message.setText("first message");
        MessageDto saveMessage1 = messageService.createMessage(user1.getId(), saveChat.getId(), message);
        message.setText("second message");
        MessageDto saveMessage2 = messageService.createMessage(user1.getId(), saveChat.getId(), message);

        assertThrows(IllegalAccessException.class, () -> messageService.searchMessagesChats(
                user2.getId(),
                saveChat.getId(),
                "",
                TypeSearch.THIS_CHAT
        ));
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