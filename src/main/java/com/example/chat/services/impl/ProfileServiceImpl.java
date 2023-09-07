package com.example.chat.services.impl;

import com.example.chat.dto.ProfileDto;
import com.example.chat.dto.UserRegisterDto;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.entities.UserEntity;
import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.example.chat.mappers.ProfileMapper;
import com.example.chat.repositories.ProfileRepository;
import com.example.chat.services.ProfileService;
import com.example.chat.services.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final SettingService settingService;

    @Override
    public ProfileDto createProfile(ProfileDto newProfile, UserEntity user) {
        newProfile.setSetting(settingService.getSettingById(1L));
        ProfileEntity profile = profileMapper.toProfileEntity(newProfile);
        profile.setUser(user);
        return profileMapper.toProfileDto(profileRepository.save(profile));
    }

    @Override
    public ProfileDto updateProfile(Long userId, UserRegisterDto updateProfile) {
        ProfileEntity profile = findProfileByUserId(userId);
        if (updateProfile.getName() != null) profile.setName(updateProfile.getName());
        if (updateProfile.getSurname() != null) profile.setSurname(updateProfile.getSurname());
        return profileMapper.toProfileDto(profileRepository.save(profile));
    }

    @Override
    public ProfileDto getProfileByUserId(Long userId) {
        return profileMapper.toProfileDto(findProfileByUserId(userId));
    }

    @Override
    public ProfileEntity getProfileByEmail(String email) {
        ProfileEntity profile = profileRepository.findByUserEmail(email);
        if (profile == null) {
            throw new NotFoundObjectException("Profile with EMAIL=" + email + " does not exist.");
        }
        return profile;
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileEntity findProfileByUserId(Long userId) {
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw new NotFoundObjectException("Profile with ID=" + userId + " does not exist.");
        }
        return profile;
    }

    private void existRights(Long userId, Long editingUserId) {
        if (!userId.equals(editingUserId)) {
            throw new AccessException("No rights to edit this user");
        }
    }
}
