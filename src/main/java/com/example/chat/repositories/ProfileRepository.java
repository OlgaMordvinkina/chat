package com.example.chat.repositories;

import com.example.chat.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    ProfileEntity findByUserId(Long userId);

    List<ProfileEntity> findAllByUserIdIn(Set<Long> ids);
}
