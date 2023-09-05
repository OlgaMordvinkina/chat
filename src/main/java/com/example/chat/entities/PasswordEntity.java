package com.example.chat.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Passwords")
public class PasswordEntity {
    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEntity(String password) {
        this.password = passwordEncoder.encode(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
}
