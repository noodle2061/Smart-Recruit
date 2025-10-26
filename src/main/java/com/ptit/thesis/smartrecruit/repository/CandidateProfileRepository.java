package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.User;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findByUser(User user);
}
