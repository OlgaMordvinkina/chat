package org.mediagate.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.KeycloakService;
import org.mediagate.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    @Value("${security.swagger.realm}")
    private String realm;

    private final UserRepository userRepository;

    private final Keycloak keycloak;

    /** Синхронизирует пользователей из Keycloak с базой данных.
     * <p>
     * Получает список всех пользователей из Keycloak и проверяет, существуют ли они в базе данных
     * по их имени пользователя (username). Если пользователь не найден в локальной базе данных,
     * то он добавляется в базу, если уже существует, то его данные синхронизируются и обновляются.
     *
     * @return строка с количеством добавленных пользователей и общим количеством пользователей из Keycloak
     * в формате "[добавлено] из [всего] пользователей добавлено."
     * <br>
     * <br>
     * Примечание: При синхронизации пользователя из Keycloak (KeycloakJwtSyncFilter.doFilterInternal())
     * из атрибутов получается username и заполняется в поле email для сохранения в БД.
     */
    @Override
    public String syncUsersWithKeycloak() {
        int updated = 0;

        List<UserRepresentation> users = this.getAllUsersFromKeycloak();
        for (UserRepresentation user : users) {
            toUserInfo(user);
            // Проверяем, существует ли пользователь
            if (!this.userRepository.existByEmail(user.getUsername())) {
                ++updated;
            }
        }
        return "[" + updated + "] из [" + users.size() + "] пользователей добавлено.";
    }

    /** Получение пользователя по email из Keycloak. */
    @Override
    public UserRepresentation getUserByEmail(String email) {
        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().searchByEmail(email, true);
            if (users != null && !users.isEmpty()) {
                return users.stream().findFirst().get();
            } else {
                throw new RuntimeException("Пользователь с email [" + email + "] не найден.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователя [" + email + "] из Keycloak по email.", e);
        }
    }

    /** Получение всех пользователей из Keycloak. */
    @Override
    public List<UserRepresentation> getAllUsersFromKeycloak() {
        try {
            return keycloak.realm(realm).users().list();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователей из Keycloak.", e);
        }
    }

    /** Получение всех ролей из Keycloak. */
    @Override
    public List<RoleRepresentation> getAllRolesFromKeycloak() {
        try {
            return keycloak.realm(realm).roles().list();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении ролей из Keycloak.", e);
        }
    }

    /** Получение всех групп из Keycloak. */
    @Override
    public List<GroupRepresentation> getAllGroupsFromKeycloak() {
        try {
            return keycloak.realm(realm).groups().groups().stream().toList();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении групп из Keycloak.", e);
        }
    }

    private UserInfo toUserInfo(UserRepresentation user) {
        return Optional.ofNullable(user)
                .map(u -> UserInfo.builder()
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .username(u.getUsername())
                        .build())
                .orElse(null);
    }

    @Override
    public List<String> getUserRoles(String keycloakUserId) {
        if (keycloakUserId == null) {
            return null;
        }
        return keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .roles()
                .getAll()
                .getRealmMappings()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserGroups(String keycloakUserId) {
        if (keycloakUserId == null) {
            return null;
        }
        return keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .groups()
                .stream()
                .map(GroupRepresentation::getName)
                .collect(Collectors.toList());
    }

}
