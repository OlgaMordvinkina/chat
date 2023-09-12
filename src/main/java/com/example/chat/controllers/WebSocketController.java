//package com.example.chat.controllers;
//
//import com.example.chat.dto.MessageDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    @MessageMapping("/chat")
////    @SendTo("/topic/news")
//    public void broadcastNews(@Payload MessageDto message) {
//
//        message.getChat().getParticipants().forEach(it ->
//                messagingTemplate.convertAndSendToUser(
//                        it.toString(),
//                        "/messages",
//                        message
//                )
//        );
//
//    }
//}
