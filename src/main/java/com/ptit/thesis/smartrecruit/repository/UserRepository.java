package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptit.thesis.smartrecruit.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserFirebaseUid(String userFirebaseUid);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}
