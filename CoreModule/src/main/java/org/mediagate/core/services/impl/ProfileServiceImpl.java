package org.mediagate.core.services.impl;

import org.mediagate.core.dto.ProfileDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.core.dto.enums.TypeBucket;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.SettingEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.mediagate.core.mappers.ProfileMapper;
import org.mediagate.core.mappers.SettingMapper;
import org.mediagate.db.repositories.ProfileRepository;
import org.mediagate.core.services.MinioService;
import org.mediagate.core.services.ProfileService;
import org.mediagate.core.services.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mediagate.db.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final SettingMapper settingMapper;
    private final SettingService settingService;
    private final MinioService minioService;

    @Override
    @Transactional
    public UserEntity createProfile(ProfileDto newProfile, UserEntity user) {
        newProfile.setSetting(settingService.getSettingById(1L));
        ProfileEntity profile = profileMapper.toProfileEntity(newProfile);
        profile.setId(user.getId());
        profile.setUser(user);
        ProfileEntity entity = profileRepository.save(profile);
        return entity.getUser();
    }

    @Override
    public ProfileDto updateProfile(Long userId, UserRegisterDto updateProfile) {
        ProfileEntity profile = findProfileByUserId(userId);
        if (updateProfile.getName() != null && !updateProfile.getName().isBlank())
            profile.setName(updateProfile.getName());
        if (updateProfile.getSurname() != null && !updateProfile.getSurname().isBlank())
            profile.setSurname(updateProfile.getSurname());
        if (updateProfile.getSettingId() != null) {
            SettingEntity setting = settingMapper.toSettingEntity(settingService.getSettingById(updateProfile.getSettingId()));

            profile.setSetting(
                    setting != null ?
                            setting :
                            settingService.createSetting(new SettingEntity(updateProfile.getSettingId(), "{sound-" + updateProfile.getSettingId() + "}"))
            );
        }
        if (updateProfile.getPhotoUser() != null) {
            String namePhoto = minioService.putFile(TypeBucket.user.name() + userId.toString(), updateProfile.getPhotoUser());
            profile.setPhoto(namePhoto);
        }
        return profileMapper.toProfileDto(profileRepository.save(profile));
    }

    @Override
    public ProfileDto updateOnlineDate(Long userId) {
        ProfileEntity profile = findProfileByUserId(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        profile.setOnlineDate(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter));
        ProfileDto profileDto = profileMapper.toProfileDto(profileRepository.save(profile));
        profileDto.setPhoto(getUrlFiles(userId, profileDto));
        return profileDto;
    }

    @Override
    public ProfileDto getProfileByUserId(Long userId) {
        ProfileDto profileDto = profileMapper.toProfileDto(findProfileByUserId(userId));
        if (profileDto.getPhoto() != null) {
            profileDto.setPhoto(getUrlFiles(userId, profileDto));
        }
        return profileDto;
    }

    private String getUrlFiles(Long userId, ProfileDto profileDto) {
        return minioService.getUrlFiles(TypeBucket.user.name() + userId, profileDto.getPhoto());
    }

    @Override
    public ProfileEntity getProfileByEmail(String email) {
        ProfileEntity profile = profileRepository.findByUserEmail(email);
        if (profile == null) {
            throw new NotFoundObjectException("Profile with EMAIL=" + email + " does not exist.");
        }
        // todo: костыль
        if (profile.getUser() == null) {
            profile.setUser(userRepository.findByEmail(email));
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
}
