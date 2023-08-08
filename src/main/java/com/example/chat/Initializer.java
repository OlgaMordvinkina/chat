package com.example.chat;

import com.example.chat.chat.dto.ChatDto;
import com.example.chat.chat.enums.Availability;
import com.example.chat.chat.services.ChatService;
import com.example.chat.message.dto.MessageDto;
import com.example.chat.message.services.MessageService;
import com.example.chat.participant.services.ParticipantService;
import com.example.chat.setting.dto.SettingDto;
import com.example.chat.setting.mapper.SettingMapper;
import com.example.chat.setting.repositories.SettingRepository;
import com.example.chat.user.dto.UserDto;
import com.example.chat.user.dto.UserRegisterDto;
import com.example.chat.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final ChatService chatService;
    private final MessageService messageService;
    private final SettingRepository settingRepository;
    private final ParticipantService participantService;
    private final UserService userService;
    private final SettingMapper settingMapper;
    private final Set<UserDto> participantsChat1 = new HashSet<>();
    private final ChatDto chat1 = new ChatDto(null, "Групповой чат-1", Availability.GROUP, participantsChat1);
    private final Set<UserDto> participantsChat2 = new HashSet<>();
    private final ChatDto chat2 = new ChatDto(null, "Приватный чат-1", Availability.PRIVATE, participantsChat2);
    private final UserRegisterDto user1 = new UserRegisterDto("petrVoropaev@mail.ru", "12345678", "12345678", "Petr", "Voropaev");
    private final UserRegisterDto user2 = new UserRegisterDto("ivanBudeyko@mail.ru", "00000000", "00000000", "Ivan", "Budeyko");
    private final UserRegisterDto user3 = new UserRegisterDto("vovaPokov@mail.ru", "11112222", "11112222", "Vova", "Pokov");
    private final MessageDto message = new MessageDto(null, 1L, 1L, LocalDateTime.now(), null, "First message", null, null, null);

    public void initial() {
        settingRepository.save(settingMapper.toSettingEntity(new SettingDto(null, "{sound-1}")));

        userService.createUser(this.user1);
        UserDto user2 = userService.createUser(this.user2);
        UserDto user3 = userService.createUser(this.user3);

        participantsChat1.add(user2);
        participantsChat1.add(user3);
        chatService.createChat(1L, chat1);

        participantService.addParticipants(1L, 1L, Set.of(3L));

        participantsChat2.add(user2);
        chatService.createChat(2L, chat2);

        participantService.addParticipants(1L, 2L, Set.of(3L));

        messageService.createMessage(message);
        message.setSenderId(2L);
        message.setChatId(1L);
        message.setText("Second message");
        messageService.createMessage(message);
    }
}
