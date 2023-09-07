package com.example.chat.services;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.UserEntity;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRegisterDto newUser);

    UserDto updateUser(Long userId, UserRegisterDto updateUser);

    void deleteUserById(Long userId, Long deletedUserId);

    UserEntity getUserById(Long userId);

    void existUserById(Long userId);

    List<UserFullDto> searchUser(Long userId, String desired);

    UserFullDto getUser(String email);
}
