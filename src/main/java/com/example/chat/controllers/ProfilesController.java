package com.example.chat.controllers;

import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.services.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ProfilesController {
    private final ProfileService service;

    @PutMapping("/{userId}/profile")
    public ProfileDto updateProfile(@PathVariable Long userId,
                                    @RequestBody UserRegisterDto updateUser) {
        log.info("PUT /user/{userId}/profile request received");
        return service.updateProfile(userId, updateUser);
    }

    @GetMapping("/{userId}/profile")
    public ProfileDto getProfile(@PathVariable Long userId) {
        log.info("GET /user/{userId}/profile request received");
        return service.getProfileByUserId(userId);
    }

    @PutMapping("/{userId}/profile/online")
    public ProfileDto updateOnlineDate(@PathVariable Long userId) {
        log.info("PUT /user/{userId}/profile request received");
        return service.updateOnlineDate(userId);
    }
}
