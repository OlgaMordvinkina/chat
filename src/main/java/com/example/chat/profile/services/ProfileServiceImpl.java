package com.example.chat.profile.services;

import com.example.chat.profile.dto.ProfileDto;
import com.example.chat.profile.entity.ProfileEntity;
import com.example.chat.profile.mapper.ProfileMapper;
import com.example.chat.profile.repositories.ProfileRepository;
import com.example.chat.setting.services.SettingService;
import com.example.chat.user.entity.UserEntity;
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
}
