package com.example.chat.repositories;

import com.example.chat.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByIdIn(Set<Long> ids);

    @Query("SELECT EXISTS(SELECT 1 FROM UserEntity WHERE email = :email)")
    boolean findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT (u.id) as id, p.surname as surname, p.name as name, u.email as email, p.photo as photo " +
            "FROM UserEntity u " +
            "JOIN ProfileEntity p ON p.user.id=u.id " +
            "WHERE u.id!=:userId " +
            "AND (lower(p.name) LIKE lower(CONCAT('%', :desired, '%')) " +
            "OR lower(p.surname) LIKE lower(CONCAT('%', :desired, '%')) " +
            "OR lower(u.email) LIKE lower(CONCAT('%', :desired, '%'))) ")
    List<Map<String, Object>> searchUser(@Param("userId") Long userId,
                                         @Param("desired") String desired);
}