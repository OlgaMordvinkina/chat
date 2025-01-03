package org.mediagate.core.services.impl;

import org.mediagate.core.dto.ProfileDto;
import org.mediagate.core.dto.UserDto;
import org.mediagate.core.dto.UserFullDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.db.enums.Role;
import org.mediagate.core.dto.enums.TypeBucket;
import org.mediagate.db.model.entities.PasswordEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.core.exceptions.AccessException;
import org.mediagate.db.exceptions.EmailUniqueException;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.mediagate.core.mappers.ProfileMapper;
import org.mediagate.core.mappers.UserMapper;
import org.mediagate.db.repositories.UserRepository;
import org.mediagate.core.services.MinioService;
import org.mediagate.core.services.ProfileService;
import org.mediagate.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final MinioService minioService;

    @Override
    @Transactional
    public UserDto createUser(UserRegisterDto newUser) {
        newUser.setEmail(newUser.getEmail().toLowerCase());

        if (userRepository.existByEmail(newUser.getEmail())) {
            throw new EmailUniqueException(newUser.getEmail());
        }

        UserDto user = userMapper.toUserDto(newUser);
        user.setRole(Role.REGISTERED);

        PasswordEntity password = new PasswordEntity(newUser.getPassword());
        UserEntity userEntity = userMapper.toUserEntity(user, password);
        ProfileDto profile = profileMapper.toProfileDto(newUser);
        UserEntity save = userRepository.save(userEntity);
        profileService.createProfile(profile, save);

        return userMapper.toUserDto(save);
    }

    @Override
    public UserDto updateUser(Long userId, UserRegisterDto updateUser) {
        UserEntity user = getUserById(userId);

        if (updateUser.getEmail() != null) {
            if (userRepository.existByEmail(updateUser.getEmail())) {
                throw new EmailUniqueException(updateUser.getEmail());
            }
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null) {
            PasswordEntity password = user.getPassword();
            password.setPassword(updateUser.getPassword());
            user.setPassword(password);
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId, Long deletedUserId) {
        existUserById(userId);
        existUserById(deletedUserId);
        existRights(userId, deletedUserId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundObjectException("User with ID=" + userId + " does not exist."));
    }

    @Override
    @Transactional(readOnly = true)
    public void existUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundObjectException("User with ID=" + userId + " does not exist.");
        }
    }

    @Override
    public List<UserFullDto> searchUser(Long userId, String desired) {
        existUserById(userId);
        return convertToUserFullDto(userRepository.searchUser(userId, desired));
    }

    @Override
    public UserFullDto getUser(String email) {
        return userMapper.toUserFullDto(profileService.getProfileByEmail(email));
    }

    private void existRights(Long userId, Long editingUserId) {
        if (!userId.equals(editingUserId)) {
            throw new AccessException("No rights to edit this user");
        }
    }

    private List<UserFullDto> convertToUserFullDto(List<Map<String, Object>> source) {
        List<UserFullDto> result = new ArrayList<>();

        var userFull = UserFullDto.builder();
        for (Map<String, Object> element : source) {
            long userId = Long.parseLong(element.get("id").toString());
            userFull.id(userId);
            userFull.fullName(element.get("surname").toString() + " " + element.get("name").toString());
            userFull.email(element.get("email").toString());
            String photo = element.get("photo") != null ? element.get("photo").toString() : null;
            if (photo != null) {
//                String file = minioService.getUrlFiles(TypeBucket.user.name() + userId, photo);
//                userFull.photo(file);
                userFull.photo(null);
            } else {
                userFull.photo(null);
            }

            result.add(userFull.build());
        }
        return result;
    }
}
