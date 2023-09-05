package com.example.chat.entities;

import com.example.chat.dto.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "password_id", referencedColumnName = "id", nullable = false)
    private PasswordEntity password;
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
