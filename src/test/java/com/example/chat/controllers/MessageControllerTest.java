//package com.example.chat.controllers;
//
//import com.example.chat.BaseApplicationTests;
//import com.example.chat.dto.ChatDto;
//import com.example.chat.dto.MessageDto;
//import com.example.chat.dto.ProfileDto;
//import com.example.chat.dto.enums.Availability;
//import com.example.chat.dto.enums.StateMessage;
//import com.example.chat.services.MessageService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.codec.binary.Base64;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class MessageControllerTest extends BaseApplicationTests {
//    private final ObjectMapper mapper;
//    private final MockMvc mvc;
//    @MockBean
//    private MessageService service;
//    private final String username = "testuser";
//    private final String password = "testpassword";
//    private final MessageDto message = new MessageDto(
//            1L,
//            new ProfileDto(2L, "Петр", "Петров", null, null, 2L, null),
//            new ChatDto(3L, "Group-chat", null, Availability.GROUP, Set.of()),
//            LocalDateTime.now(),
//            StateMessage.SENT,
//            "New message",
//            null,
//            null,
//            List.of(),
//            null
//    );
//
//    @Test
//    void message_auth_exception() throws Exception {
//        when(service.createMessage(anyLong(), anyLong(), any())).thenReturn(message);
//
//        mvc.perform(post("/users/2/chats/3/messages")
//                        .content(mapper.writeValueAsString(message)))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void createMessage() throws Exception {
//        when(service.createMessage(anyLong(), anyLong(), any())).thenReturn(message);
//
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        mvc.perform(post("/users/2/chats/3/messages")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Basic " + encodedCredentials)
//                        .content(mapper.writeValueAsString(message)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.state").value(StateMessage.SENT.name()))
//                .andExpect(jsonPath("$.text").value("New message"));
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void updateMessage() throws Exception {
//        message.setText("Update message");
//        when(service.updateMessage(any())).thenReturn(message);
//
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        mvc.perform(put("/users/2/chats/3/messages")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Basic " + encodedCredentials)
//                        .content(mapper.writeValueAsString(message)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.state").value(StateMessage.SENT.name()))
//                .andExpect(jsonPath("$.text").value("Update message"));
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void updateStateMessage() throws Exception {
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        mvc.perform(put("/users/2/chats/3/messages/state")
//                        .header("Authorization", "Basic " + encodedCredentials))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void deleteMessage() throws Exception {
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        when(service.deleteMessage(anyLong(), anyLong(), anyLong())).thenReturn(message);
//
//        mvc.perform(delete("/users/1/chats/3/messages/1")
//                        .header("Authorization", "Basic " + encodedCredentials))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void getMessages() throws Exception {
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        List<MessageDto> messages = new ArrayList<>();
//        messages.add(message);
//        message.setId(2L);
//        messages.add(message);
//
//        when(service.getMessages(anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(messages);
//
//        mvc.perform(get("/users/1/chats/3/messages")
//                        .param("page", "1")
//                        .param("size", "15")
//                        .content(mapper.writeValueAsString(messages))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Basic " + encodedCredentials))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void searchMessagesAllChats() throws Exception {
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        List<MessageDto> messages = new ArrayList<>();
//        messages.add(message);
//        message.setId(2L);
//        messages.add(message);
//
//        when(service.searchMessagesChats(anyLong(), anyLong(), anyString(), any())).thenReturn(messages);
//
//        mvc.perform(get("/users/1/chats/3/messages/search")
//                        .param("desired", "message")
//                        .content(mapper.writeValueAsString(messages))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Basic " + encodedCredentials))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", password = "testpassword", roles = "REGISTERED")
//    void searchMessagesThisChat() throws Exception {
//        String credentials = username + ":" + password;
//        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//
//        List<MessageDto> messages = new ArrayList<>();
//        messages.add(message);
//        message.setId(2L);
//        messages.add(message);
//
//        when(service.searchMessagesChats(anyLong(), anyLong(), anyString(), any())).thenReturn(messages);
//
//        mvc.perform(get("/users/1/chats/3/messages/search")
//                        .param("desired", "message")
//                        .content(mapper.writeValueAsString(messages))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Basic " + encodedCredentials))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//    }
//}