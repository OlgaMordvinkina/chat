package com.example.chat.user.services;

import com.example.chat.user.dto.UserDto;
import com.example.chat.user.dto.UserRegisterDto;

public interface UserService {
    UserDto createUser(UserRegisterDto newUser);
}
