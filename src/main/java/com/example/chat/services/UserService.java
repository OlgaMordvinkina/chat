package com.example.chat.services;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserRegisterDto;

public interface UserService {
    UserDto createUser(UserRegisterDto newUser);

    UserDto updateUser(Long userId, Long updatedUserId, UserRegisterDto updateUser);

    void deleteUserById(Long userId, Long deletedUserId);
}
