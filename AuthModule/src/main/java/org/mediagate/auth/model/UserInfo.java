package org.mediagate.auth.model;

import org.mediagate.db.model.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    /**
     * Идентификатор в БД
     */
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> groups;
    /** Роли пользователя в формате ":user:pm:admin:" */
    private String globalRoles;
    /** Группы пользователя в формате ":user:pm:admin:" */
    private String globalGroups;

    public static UserInfo from(UserEntity user) {
        return Objects.isNull(user) ? null : UserInfo.builder()
                .id(user.getId())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public static UserEntity to(UserInfo userInfo) {
        UserEntity user = new UserEntity();
        user.setId(userInfo.getId());
        user.setEmail(userInfo.getEmail());
//        user.setFirstName(userInfo.getFirstName());
//        user.setLastName(userInfo.getLastName());
//        user.setGlobalRoles_csv(userInfo.getGlobalRoles());
//        user.setGlobalGroups_csv(userInfo.getGlobalGroups());
        return user;
    }
}
