package com.example.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    private static Initializer initializer;

    @Autowired
    public void setInitialLoader(Initializer initiator) {
        ChatApplication.initializer = initiator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        initializer.initial();
    }

}
