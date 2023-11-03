package com.example.chat.repositories;

import com.example.chat.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    ProfileEntity findByUserId(Long userId);

    @Query("SELECT p " +
            "FROM ProfileEntity p " +
            "JOIN p.user u " +
            "WHERE u.email=:email")
    ProfileEntity findByUserEmail(@Param("email") String email);

    List<ProfileEntity> findAllByUserIdIn(Set<Long> ids);
}
