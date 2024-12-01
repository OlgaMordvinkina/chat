package org.mediagate.core.services;

import org.mediagate.core.dto.UserDto;
import org.mediagate.core.dto.UserFullDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.db.model.entities.UserEntity;

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
