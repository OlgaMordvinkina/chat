package com.example.chat.user.services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}