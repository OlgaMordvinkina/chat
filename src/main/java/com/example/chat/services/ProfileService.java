package com.example.chat.services;

import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.entities.UserEntity;

public interface ProfileService {
    UserEntity createProfile(ProfileDto newProfile, UserEntity user);

    ProfileDto updateProfile(Long userId, UserRegisterDto updateProfile);

    ProfileEntity findProfileByUserId(Long userId);

    ProfileDto getProfileByUserId(Long userId);

    ProfileEntity getProfileByEmail(String email);

    ProfileDto updateOnlineDate(Long userId);
}
