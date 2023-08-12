package com.example.chat.user.services;

import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.password.entity.PasswordEntity;
import com.example.chat.profile.dto.ProfileDto;
import com.example.chat.profile.mapper.ProfileMapper;
import com.example.chat.profile.services.ProfileService;
import com.example.chat.user.dto.UserDto;
import com.example.chat.user.dto.UserRegisterDto;
import com.example.chat.user.entity.UserEntity;
import com.example.chat.user.enums.Role;
import com.example.chat.user.exceptions.EmailUniqueException;
import com.example.chat.user.mapper.UserMapper;
import com.example.chat.user.repositories.UserRepository;
import com.example.chat.utils.ValidationsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ValidationsUtils utils;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    @Override
    public UserDto createUser(UserRegisterDto newUser) {
        if (userRepository.findByEmail(newUser.getEmail())) {
            throw new EmailUniqueException(newUser.getEmail());
        }

        UserDto user = userMapper.toUserDto(newUser);
        user.setRole(Role.REGISTERED);

        PasswordEntity password = new PasswordEntity(newUser.getPassword());

        UserEntity userEntity = userMapper.toUserEntity(user, password);
        UserEntity saveUser = userRepository.save(userEntity);

        ProfileDto profile = profileMapper.toProfileDto(newUser);
        profile.setUserId(saveUser.getId());

        profileService.createProfile(profile, userEntity);

        return userMapper.toUserDto(saveUser);
    }

    @Override
    public UserDto updateUser(Long userId, Long updatedUserId, UserRegisterDto updateUser) {
        UserEntity user = utils.getUserById(userId);
        utils.existUserById(updatedUserId);
        existRights(userId, updatedUserId);
        if (updateUser.getEmail() != null) {
            if (userRepository.findByEmail(updateUser.getEmail())) {
                throw new EmailUniqueException(updateUser.getEmail());
            }
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null) {
            user.setPassword(new PasswordEntity(updateUser.getPassword()));
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long userId, Long deletedUserId) {
        utils.existUserById(userId);
        utils.existUserById(deletedUserId);
        existRights(userId, deletedUserId);
        userRepository.deleteById(userId);
    }

    private void existRights(Long userId, Long editingUserId) {
        if (!userId.equals(editingUserId)) {
            throw new AccessException("No rights to edit this user");
        }
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundObjectException("User with ID=" + userId + " does not exist."));
    }

    private void existUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundObjectException("User with ID=" + userId + " does not exist.");
        }
    }
}
