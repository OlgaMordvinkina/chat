package com.example.chat.profile.services;

import com.example.chat.profile.dto.ProfileDto;
import com.example.chat.user.entity.UserEntity;

public interface ProfileService {
    ProfileDto createProfile(ProfileDto newProfile, UserEntity user);
}
