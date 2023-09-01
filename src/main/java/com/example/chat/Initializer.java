package com.example.chat;

import com.example.chat.controllers.ChatController;
import com.example.chat.controllers.MessageController;
import com.example.chat.dto.*;
import com.example.chat.dto.enums.Availability;
import com.example.chat.mappers.SettingMapper;
import com.example.chat.repositories.SettingRepository;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer {
    private final SettingRepository settingRepository;
    private final UserService userService;
    private final SettingMapper settingMapper;
    private final ChatController chatController;
    private final MessageController messageController;

    private final ChatDto chat1 = new ChatDto(null, null, Availability.PRIVATE, null);
    private final ChatDto chat2 = new ChatDto(null, null, Availability.PRIVATE, null);
    private final ChatDto chat3 = new ChatDto(null, null, Availability.PRIVATE, null);
    private final ChatDto chat4 = new ChatDto(null, "Групповой чатик", Availability.GROUP, null);
    private final ChatDto chat5 = new ChatDto(null, "Групповой", Availability.GROUP, null);
    private final UserRegisterDto user1 = new UserRegisterDto("петренко@mail.ru", "12345678", "12345678", "Полина", "Петренко");
    private final UserRegisterDto user2 = new UserRegisterDto("мануйлова@mail.ru", "00000000", "00000000", "Валентина", "Мануйлова");
    private final UserRegisterDto user3 = new UserRegisterDto("кузьмина@mail.ru", "11112222", "11112222", "Екатерина", "Кузьмина");
    private final UserRegisterDto user4 = new UserRegisterDto("орлова@mail.ru", "admin000", "admin000", "Ольга", "Орлова");
    private final MessageDto message = new MessageDto();

    public void initial() {
        settingRepository.save(settingMapper.toSettingEntity(new SettingDto(null, "{sound-1}")));

        UserDto user1 = userService.createUser(this.user1);
        UserDto user2 = userService.createUser(this.user2);
        UserDto user3 = userService.createUser(this.user3);
        UserDto user4 = userService.createUser(this.user4);
        userService.createUser(new UserRegisterDto("Белов@mail.ru", "02516486", "02516486", "Олег", "Белов"));
        userService.createUser(new UserRegisterDto("Перебейло@mail.ru", "16548951", "16548951", "Марина", "Перебейло"));
        userService.createUser(new UserRegisterDto("Крюковский@mail.ru", "02322548", "02322548", "Даниил", "Крюковский"));
        userService.createUser(new UserRegisterDto("Брошка@mail.ru", "62515881", "62515881", "Рита", "Брошка"));

        chat1.setParticipants(Set.of(user1.getId()));
        chatController.createChat(4L, chat1);
        chat2.setParticipants(Set.of(user2.getId()));
        chatController.createChat(4L, chat2);
        chat3.setParticipants(Set.of(user3.getId()));
        chatController.createChat(2L, chat3);
        chat4.setParticipants(Set.of(user2.getId(), user3.getId()));
        chatController.createChat(4L, chat4);
        chat5.setParticipants(Set.of(user2.getId(), user3.getId()));
        chatController.createChat(4L, chat5);

        message.setText("Привет");
        messageController.createMessage(1L, 1L, message);
        message.setText("Привет");
        messageController.createMessage(4L, 1L, message);
        message.setText("Как дела?");
        messageController.createMessage(1L, 1L, message);
        message.setText("Изначально повесть задумывалась более сложной и объёмной, но редактор Уолтер Брэдбери (однофамилец) предложил писателю: «Возьми эту книгу за уши и потяни в разные стороны. Она разорвётся на две части. Каждая вторая [история] выпадет");
        messageController.createMessage(1L, 1L, message);
        message.setText("Ничего, а ты");
        messageController.createMessage(4L, 1L, message);
        message.setText("Действие происходит летом 1928 года в вымышленном городе Гринтауне, штат Иллинойс, прототипом которого является родной город Брэдбери — Уокиган. В центре сюжета — братья Сполдинг: Дуглас (12 лет) и Том (10 лет). Повесть состоит из вереницы историй, приключившихся в маленьком городке за три летних месяца с братьями, их родственниками, соседями, друзьями, знакомыми.");
        messageController.createMessage(2L, 2L, message);
        message.setText("В 1971 году экипаж космического корабля «Аполлон-15» присвоил одному из кратеров Луны название Одуванчик");
        messageController.createMessage(2L, 2L, message);
        message.setText("честь повести «Вино из одуванчиков»[2] (название пока не утверждено Международным астрономическим союзом).");
        messageController.createMessage(4L, 2L, message);
        message.setText("делаю вид, что пишу что-то важное");
        messageController.createMessage(2L, 2L, message);

        message.setText("Данный термин переводится как «печенье», но поскольку это название лишь сбивает с толку, его оставляют без перевода. Cookie можно использовать для сохранения даты последнего посещения читателя, паролей, а также любой информации о действиях посетителя на сайте. Подобное применение позволяет персонализировать сайт и сделать его более удобным для посетителей.");
        messageController.createMessage(4L, 2L, message);
        message.setText("допустимо задавать и в других единицах CSS, например, пикселах (px), процентах (%) и др. В данном примере для первого абзаца установлен полуторный интерлиньяж, а для второго — межстрочное расстояние равно");
        messageController.createMessage(4L, 2L, message);
        message.setText("Спасибо очень много полезной информации у Вас нашла, но то что мне нужно не вижу.");
        messageController.createMessage(2L, 2L, message);
        message.setText("Треш");
        messageController.createMessage(2L, 2L, message);

        message.setText("блаблабла");
        messageController.createMessage(2L, 4L, message);
        message.setText("спрыгнула с высокого дерева и... Это может быть полезно для выравнивания элементов модальных окон, заголовков или других элементов, которые имеют разный размер текста.");
        messageController.createMessage(4L, 4L, message);
        message.setText("2013 года. Дата обращения: 11 декабря 2013.");
        messageController.createMessage(3L, 4L, message);

//        messageController.updateMessage(2L, 2L, 2L, message);
    }
}
