package com.example.chat.repositories.impl;

import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.dto.enums.StateMessage;
import com.example.chat.repositories.CustomChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Objects;

public class CustomChatRepositoryImpl implements CustomChatRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{T(com.example.chat.utils.ResourceReaderUtils).readFileToString('classpath:getReviews.sql')}")
    private String getReviewsSql;

    @Override
    public List<ChatPreviewDto> getReviews(Long userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);

        return jdbcTemplate.query(getReviewsSql, parameters,
                (rs, rowNum) -> {
                    var preview = ChatPreviewDto.builder();

                    long chatId = rs.getLong("chatId");
                    preview.chatId(chatId);
                    preview.messageId(rs.getLong("messageId"));
                    preview.title(rs.getString("title"));
                    Long senderId = rs.getLong("senderId");
                    preview.senderId(senderId);

                    String dateLastMsg = rs.getString("dateLastMessage");
                    if (!Objects.equals(dateLastMsg, null)) {
                        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                .appendPattern("yyyy-MM-dd HH:mm:ss")
                                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 6, true)
                                .toFormatter();
                        LocalDateTime dateLastMessage = LocalDateTime.parse(dateLastMsg, formatter);

                        LocalDate currentDate = LocalDate.now();
                        if (dateLastMessage.toLocalDate().isEqual(currentDate)) {
                            preview.dateLastMessage(String.format("%02d:%02d:%d", dateLastMessage.getHour(), dateLastMessage.getMinute(), dateLastMessage.getSecond()));
                        } else {
                            preview.dateLastMessage(String.format("%02d.%02d.%d", dateLastMessage.getDayOfMonth(), dateLastMessage.getMonthValue(), dateLastMessage.getYear()));
                        }
                    } else {
                        preview.dateLastMessage("");
                    }
                    String stateMessage = rs.getString("stateMessage");
                    preview.stateMessage(!Objects.equals(stateMessage, null) ? StateMessage.valueOf(stateMessage) : null);

                    Long companionId = rs.getLong("companionId");
                    preview.companionId(companionId != 0L ? companionId : null);

                    String nameFile = rs.getString("nameFile");
                    preview.photo(nameFile);

                    String lastMessage = rs.getString("text");
                    preview.lastMessage(lastMessage);

                    preview.unreadMessages(0L);

                    return preview.build();
                }
        );
    }
}
