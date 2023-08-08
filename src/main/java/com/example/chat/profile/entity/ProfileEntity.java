package com.example.chat.profile.entity;

import com.example.chat.setting.entity.SettingEntity;
import com.example.chat.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Profiles")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(name = "photo_id")
    private String photo;
    @ManyToOne
    @JoinColumn(name = "setting_id", referencedColumnName = "id")
    private SettingEntity setting;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
