package com.example.chat.repositories;

import com.example.chat.entities.UserEntity;
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

    @Query("SELECT DISTINCT (u.id), p.surname, p.name, u.email, p.photo " +
            "FROM UserEntity u " +
            "JOIN ProfileEntity p ON p.user.id=u.id " +
            "WHERE u.id!=:userId " +
            "AND (lower(p.name) LIKE lower(CONCAT('%', :desired, '%')) " +
            "OR lower(p.surname) LIKE lower(CONCAT('%', :desired, '%')) " +
            "OR lower(u.email) LIKE lower(CONCAT('%', :desired, '%'))) ")
    String[] searchUser(Long userId, String desired);
}