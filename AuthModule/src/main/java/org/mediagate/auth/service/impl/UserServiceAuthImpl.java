package org.mediagate.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.mediagate.auth.service.KeycloakService;
import org.mediagate.auth.service.UserServiceAuth;
import org.mediagate.db.enums.Role;
import org.mediagate.db.exceptions.EmailUniqueException;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.SettingEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.ProfileRepository;
import org.mediagate.db.repositories.SettingRepository;
import org.mediagate.db.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceAuthImpl implements UserServiceAuth {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SettingRepository settingRepository;

    private final KeycloakService keycloakService;

    @Override
    public UserRepresentation getUserByEmailFromKeycloak(String email) {
        return keycloakService.getUserByEmail(email);
    }

    @Override
    @Transactional
    public UserEntity createUserInDB(UserRepresentation userFromKK) {
        if (userRepository.existByEmail(userFromKK.getEmail())) {
            throw new EmailUniqueException(userFromKK.getEmail());
        }
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userFromKK.getEmail());
        newUser.setRole(Role.REGISTERED);

        UserEntity save = userRepository.save(newUser);
        return createProfileInDB(userFromKK, save).getUser();
    }

    private ProfileEntity createProfileInDB(UserRepresentation userFromKK, UserEntity user) {
        ProfileEntity profileUser = new ProfileEntity();

        profileUser.setName(userFromKK.getFirstName());
        profileUser.setSurname(userFromKK.getLastName());
        Optional<SettingEntity> setting = this.settingRepository.findById(1L);
        setting.ifPresent(profileUser::setSetting);
        profileUser.setId(user.getId());
        profileUser.setUser(user);

        return profileRepository.save(profileUser);
    }
}
