package com.example.chat.controllers;

import com.example.chat.BaseApplicationTests;
import com.example.chat.dto.AttachmentDto;
import com.example.chat.dto.ChatDto;
import com.example.chat.dto.ChatFullDto;
import com.example.chat.dto.ChatPreviewDto;
import com.example.chat.dto.enums.Availability;
import com.example.chat.services.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ChatControllerTest extends BaseApplicationTests {
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    @MockBean
    private final ChatService service;
    private final String username = "testuser";
    private final String password = "testpassword";
    private final ChatDto chat = new ChatDto(1L, "group-chat-1", null, Availability.GROUP, Set.of(2L));

    @Test
    void message_auth_exception() throws Exception {
        when(service.createChat(anyLong(), any())).thenReturn(chat);

        mvc.perform(post("/users/2/chats/3")
                        .content(mapper.writeValueAsString(chat)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void createChat() throws Exception {
        when(service.createChat(anyLong(), any())).thenReturn(chat);

        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        mvc.perform(post("/users/2/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + encodedCredentials)
                        .content(mapper.writeValueAsString(chat)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value(Availability.GROUP.name()));
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void deleteChat() throws Exception {
        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        mvc.perform(delete("/users/1/chats/1")
                        .header("Authorization", "Basic " + encodedCredentials))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void getChatPreviews() throws Exception {
        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        ChatPreviewDto chatPreview = new ChatPreviewDto();
        chatPreview.setChatId(1L);
        List<ChatPreviewDto> chatPreviews = new ArrayList<>();
        chatPreviews.add(chatPreview);
        chatPreview.setChatId(2L);
        chatPreviews.add(chatPreview);

        when(service.getChatPreviews(anyLong())).thenReturn(chatPreviews);

        mvc.perform(get("/users/1/chats/previews")
                        .content(mapper.writeValueAsString(chatPreviews))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + encodedCredentials))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void getChat() throws Exception {
        ChatFullDto chatFull = new ChatFullDto();
        chatFull.setId(1L);
        when(service.getChat(anyLong(), anyLong())).thenReturn(chatFull);

        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        mvc.perform(get("/users/2/chats/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + encodedCredentials)
                        .content(mapper.writeValueAsString(chatFull)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void getAttachmentsChat() throws Exception {
        List<AttachmentDto> attachments = List.of(new AttachmentDto());
        when(service.getAttachmentsChat(anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(attachments);

        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        mvc.perform(get("/users/2/chats/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(attachments))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + encodedCredentials))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
    void updatePhotoChat() throws Exception {
        when(service.updatePhotoChat(anyLong(), anyLong(), anyString())).thenReturn(chat);

        String credentials = username + ":" + password;
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        mvc.perform(put("/users/2/chats/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + encodedCredentials)
                        .content(mapper.writeValueAsString(chat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value(Availability.GROUP.name()));
    }
}