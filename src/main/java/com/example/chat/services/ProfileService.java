package com.example.chat.services;

import com.example.chat.dto.ProfileDto;
import com.example.chat.entities.UserEntity;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto newProfile, UserEntity user);
}
