package com.example.chat.controllers;

import com.example.chat.dto.UserDto;
import com.example.chat.dto.UserFullDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
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
    public UserDto updateUser(@PathVariable Long userId,
                              @RequestBody UserRegisterDto updateUser) {
        log.info("PUT /users/registration/{userId} request received");
        return service.updateUser(userId, updateUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId, Long deletedUserId) {
        log.info("DELETE /users/registration/{userId} request received");
        service.deleteUserById(userId, deletedUserId);
    }

    @GetMapping("/{userId}/search")
    public List<UserFullDto> searchUser(@PathVariable Long userId, @Valid @NotBlank @RequestParam String desired) {
        log.info("GET /users/{userId}/search request received");
        return service.searchUser(userId, desired);
    }

    @GetMapping("/{userId}")
    public UserFullDto getUser(@PathVariable Long userId) {
        log.info("GET /users/{userId}/search request received");
        return service.getUser(userId);
    }
}
