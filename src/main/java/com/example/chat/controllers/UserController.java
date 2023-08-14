package com.example.chat.controllers;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserDto createUser(@RequestBody UserRegisterDto newUser) {
        log.info("POST /users/registration request received");
        return service.createUser(newUser);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, Long updatedUserId, @RequestBody UserRegisterDto updateUser) {
        log.info("PUT /users/registration/{userId} request received");
        return service.updateUser(userId, updatedUserId, updateUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId, Long deletedUserId) {
        log.info("DELETE /users/registration/{userId} request received");
        service.deleteUserById(userId, deletedUserId);
    }
}
