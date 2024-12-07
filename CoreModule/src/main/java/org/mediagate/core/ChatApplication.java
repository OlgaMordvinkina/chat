package org.mediagate.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan({"org.mediagate.db.model"})
@EnableJpaRepositories(basePackages = {"org.mediagate.db.repositories"})
@ComponentScan(basePackages = {"org.mediagate.core", "org.mediagate.auth"})
@SpringBootApplication
public class ChatApplication {
    private static Initializer initializer;

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        initializer.initial();
    }

    @Autowired
    public void setInitialLoader(Initializer initiator) {
        ChatApplication.initializer = initiator;
    }

}
