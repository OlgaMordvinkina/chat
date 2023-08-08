package com.example.chat.user.repositories;

import com.example.chat.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByIdIn(Set<Long> ids);

    @Query("SELECT EXISTS(SELECT 1 FROM UserEntity WHERE email = :email)")
    boolean findByEmail(String email);
}
