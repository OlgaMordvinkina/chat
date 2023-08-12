package com.example.chat.user.services;

import com.example.chat.user.dto.UserDto;
import com.example.chat.user.dto.UserRegisterDto;

public interface UserService {
    UserDto createUser(UserRegisterDto newUser);

    UserDto updateUser(Long userId, Long updatedUserId, UserRegisterDto updateUser);

    void deleteUserById(Long userId, Long deletedUserId);
}
