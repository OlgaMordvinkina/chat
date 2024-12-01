package org.mediagate.core.services;

import org.mediagate.core.dto.ProfileDto;
import org.mediagate.core.dto.UserRegisterDto;
import org.mediagate.db.model.entities.ProfileEntity;
import org.mediagate.db.model.entities.UserEntity;

public interface ProfileService {
    UserEntity createProfile(ProfileDto newProfile, UserEntity user);

    ProfileDto updateProfile(Long userId, UserRegisterDto updateProfile);

    ProfileEntity findProfileByUserId(Long userId);

    ProfileDto getProfileByUserId(Long userId);

    ProfileEntity getProfileByEmail(String email);

    ProfileDto updateOnlineDate(Long userId);
}
