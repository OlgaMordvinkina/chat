package org.mediagate.db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
//@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Settings")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SettingEntity extends ABaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
    String setting;


    public SettingEntity(Long id, String setting) {
        super.id = id;
        this.setting = setting;
    }
}
