package com.example.chat.services.impl;

import com.example.chat.dto.ProfileDto;
import com.example.chat.entities.ProfileEntity;
import com.example.chat.entities.UserEntity;
import com.example.chat.exceptions.AccessException;
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
    private final ProfileRepository repository;
    private final ProfileMapper mapper;
    private final SettingService settingService;

    @Override
    public ProfileDto createProfile(ProfileDto newProfile, UserEntity user) {
        newProfile.setSetting(settingService.getSettingById(1L));
        ProfileEntity entity = mapper.toProfileEntity(newProfile);
        entity.setUser(user);
        return mapper.toProfileDto(repository.save(entity));
    }

//    @Override
//    public ProfileDto updateProfile() {
//
//    }

    private void existRights(Long userId, Long editingUserId) {
        if (!userId.equals(editingUserId)) {
            throw new AccessException("No rights to edit this user");
        }
    }


}
