package org.mediagate.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.UserEntity;

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
    private String username;
    private List<String> groups;
    private List<String> globalRoles;
    private List<String> globalGroups;

    public static UserInfo from(ProfileEntity profile) {
        UserEntity user = profile.getUser();
        return Objects.isNull(user) ? null : UserInfo.builder()
                .id(user.getId())
                .firstName(profile.getName())
                .lastName(profile.getSurname())
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
