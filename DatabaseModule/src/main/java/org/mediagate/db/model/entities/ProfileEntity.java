package org.mediagate.db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Profiles")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProfileEntity extends ABaseEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "first_name")
    String name;
    String surname;
    String photo;
    @ManyToOne
    @JoinColumn(name = "setting_id", referencedColumnName = "id")
    SettingEntity setting;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;
    @Column(name = "online_date")
    LocalDateTime onlineDate;
}
