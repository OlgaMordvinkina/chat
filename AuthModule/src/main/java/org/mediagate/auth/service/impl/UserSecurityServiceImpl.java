package org.mediagate.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.mediagate.auth.config.KeycloakJwtAttributes;
import org.mediagate.auth.context.SecurityContext;
import org.mediagate.auth.exceptions.AccessControlException;
import org.mediagate.auth.exceptions.AccessControlExceptionCode;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.UserSecurityService;
import org.mediagate.auth.service.UserServiceAuth;
import org.mediagate.auth.util.KeycloakJwtAttributeName;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.ProfileRepository;
import org.mediagate.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {
    @Value("${security.principal_attribute}")
    private KeycloakJwtAttributeName PRINCIPAL_ATTRIBUTE;

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    private final UserServiceAuth userService;

    @Transactional
    public void syncUser(UserInfo userInfo) {
//      todo: синхронизируем только если юзер уже создан в кейклоке или в бд
        try {
            UserEntity user;
            if (Objects.isNull(userInfo)) {
                throw new AccessControlException(AccessControlExceptionCode.USER_NOT_AUTHENTICATED, "User is null");
            }

            UserRepresentation userFromKK = userService.getUserByEmailFromKeycloak(userInfo.getEmail());
            user = findByEmail(userInfo.getEmail());
            // Если в кк существует, а в бд нет, то надо создать в бд
            if (Objects.nonNull(userFromKK) && Objects.isNull(user)) {
                userService.createUserInDB(userFromKK);
            }

            if (Objects.isNull(user)) {
                user = new UserEntity();
            }
            user.setEmail(userInfo.getEmail());
            log.debug("Синхронизация данных пользователя {id: {}, email: {}} с Keycloak прошла успешно", user.getId(), userInfo.getEmail());
        } catch (Exception exception) {
            log.error("Ошибка при синхронизации данных пользователя и групп Keycloak с БД", exception);
            throw exception;
        }
    }

    public UserInfo loggedUser() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContext.authentication();
        KeycloakJwtAttributes jwtAttributes = new KeycloakJwtAttributes(jwtAuthenticationToken, PRINCIPAL_ATTRIBUTE);
        ProfileEntity userDb = findProfileByEmail(jwtAttributes.username());
        return UserInfo.from(userDb);
    }

    public UserEntity loggedDbUser() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContext.authentication();
        KeycloakJwtAttributes jwtAttributes = new KeycloakJwtAttributes(jwtAuthenticationToken, PRINCIPAL_ATTRIBUTE);
        return findByEmail(jwtAttributes.username());
    }

    private UserEntity findByEmail(@NonNull String email) {
        return userRepository.findByEmail(email);
    }

    private ProfileEntity findProfileByEmail(@NonNull String email) {
        return profileRepository.findByUserEmail(email);
    }

}
