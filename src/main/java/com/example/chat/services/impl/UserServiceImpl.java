package com.example.chat.services.impl;

import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.dto.enums.Role;
import com.example.chat.entities.PasswordEntity;
import com.example.chat.entities.UserEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.EmailUniqueException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.mappers.ProfileMapper;
import com.example.chat.mappers.UserMapper;
import com.example.chat.repositories.UserRepository;
import com.example.chat.services.ProfileService;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    @Override
    public UserDto createUser(UserRegisterDto newUser) {
        newUser.setEmail(newUser.getEmail().toLowerCase());
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
    public UserDto updateUser(Long userId, UserRegisterDto updateUser) {
        UserEntity user = getUserById(userId);
//        existUserById(updatedUserId);
//        existRights(userId, updatedUserId);
//        if (updateUser.getName() != null || updateUser.getSurname() != null) {
//            profileService.updateProfile(userId, updateUser);
//        }

        if (updateUser.getEmail() != null) {
            if (userRepository.findByEmail(updateUser.getEmail())) {
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
    public UserFullDto getUser(Long userId) {
        return userMapper.toUserFullDto(profileService.findProfileByUserId(userId));
    }

    private void existRights(Long userId, Long editingUserId) {
        if (!userId.equals(editingUserId)) {
            throw new AccessException("No rights to edit this user");
        }
    }

    private List<UserFullDto> convertToUserFullDto(String[] source) {
        List<UserFullDto> result = new ArrayList<>();

        var userFull = UserFullDto.builder();
        for (String element : source) {
            String[] chatPreview = element.split(",");

            userFull.id(Long.parseLong(chatPreview[0]));
            userFull.fullName(chatPreview[1] + " " + chatPreview[2]);
            userFull.email(chatPreview[3]);

            result.add(userFull.build());
        }
        return result;
    }
}