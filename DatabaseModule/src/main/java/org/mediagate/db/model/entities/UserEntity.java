package org.mediagate.db.model.entities;

import lombok.experimental.SuperBuilder;
import org.mediagate.db.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserEntity extends ABaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
    @Column(unique = true)
    String email;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "password_id", referencedColumnName = "id", nullable = false)
    PasswordEntity password;
    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    Role role;
}
