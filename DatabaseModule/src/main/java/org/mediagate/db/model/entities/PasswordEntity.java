package org.mediagate.db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Passwords")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PasswordEntity extends ABaseEntity {
    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "user_password")
    String password;

    public PasswordEntity(String password) {
        this.password = passwordEncoder.encode(password);
    }
}
