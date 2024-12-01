package org.mediagate.auth.service.impl;

import org.mediagate.auth.config.KeycloakJwtAttributes;
import org.mediagate.auth.context.SecurityContext;
import org.mediagate.auth.exceptions.AccessControlException;
import org.mediagate.auth.exceptions.AccessControlExceptionCode;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.UserSecurityService;
import org.mediagate.auth.util.KeycloakJwtAttributeName;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final UserRepository userRepository;

//    private final IGroupDataService groupDataService;

    @Value("${security.principal_attribute}")
    private KeycloakJwtAttributeName PRINCIPAL_ATTRIBUTE;

    @Transactional
    public void syncUser(UserInfo userInfo) {
        try {
            UserEntity user;
            if (Objects.isNull(userInfo)) {
                throw new AccessControlException(AccessControlExceptionCode.USER_NOT_AUTHENTICATED, "User is null");
            }
            user = findByEmail(userInfo.getEmail());
            if (Objects.isNull(user)) {
                user = new UserEntity();
            }
//            user.setFirstName(userInfo.getFirstName());
//            user.setLastName(userInfo.getLastName());
            user.setEmail(userInfo.getEmail());
//            user.setGlobalRoles_csv(userInfo.getGlobalRoles());
//            user.setGlobalGroups_csv(userInfo.getGlobalGroups());

            userRepository.save(user);
//            Set<Group> groups = syncGroups(new HashSet<>(userInfo.getGroups()));
            //todo sync if needed
//            syncUserWithGroups(user, groups);
            log.debug("Синхронизация данных пользователя {id: {}, email: {}} с Keycloak прошла успешно", user.getId(), userInfo.getEmail());
        } catch (Exception exception) {
            log.error("Ошибка при синхронизации данных пользователя и групп Keycloak с БД", exception);
            throw exception;
        }
    }

    public UserInfo loggedUser() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContext.authentication();
        KeycloakJwtAttributes jwtAttributes = new KeycloakJwtAttributes(jwtAuthenticationToken, PRINCIPAL_ATTRIBUTE);
        UserEntity userDb = findByEmail(jwtAttributes.username());
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

//    private Set<Group> syncGroups(@NonNull Set<String> groups) {
//        Filter groupFilter = Group.createFilter()
//                .in(Group.Fields.name, groups)
//                .build();
//        Set<Group> groupEntities = new HashSet<>(groupDataService.findAllByFilter(groupFilter));
//        Set<Group> createdGroups = createNotExistingGroups(groupEntities, groups);
//        if (createdGroups.isEmpty()) {
//            return groupEntities;
//        }
//        createdGroups.addAll(groupEntities);
//        return createdGroups;
//    }

//    private Set<Group> createNotExistingGroups(@NonNull Set<Group> groupsDb, @NonNull Set<String> groupsKc) {
//        Predicate<String> predicate = group -> {
//            for (Group g : groupsDb) {
//                if (group.equals(g.getName())) {
//                    return false;
//                }
//            }
//            return true;
//        };
//        List<String> notExistingGroups = groupsKc.stream().filter(predicate).toList();
//
//        List<Group> groupsToCreate = new ArrayList<>();
//        for (String notExistingGroup : notExistingGroups) {
//            Group group = new Group();
//            group.setName(notExistingGroup);
//            groupsToCreate.add(group);
//        }
//        return groupDataService.saveAll(groupsToCreate).stream().map(EntitySaveStatus::getEntity).collect(Collectors.toSet());
//    }
//
//    private void syncUserWithGroups(@NonNull User user, Set<Group> groups) {
//        user.setGroups(groups);
//        userRepository.save(user).getEntity();
//    }
}
