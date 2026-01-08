package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.response.CandidateStatResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.User;


@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long>, CandidateProfileRepositoryCustom {
    Optional<CandidateProfile> findByUser(User user);

    @Query("SELECT c.fullName FROM CandidateProfile c WHERE c.user = :user")
    String findFullNameByUser(User user);

    @Query("SELECT c.avatarUrl FROM CandidateProfile c WHERE c.user = :user")
    String findAvatarByUser(User user);

    @Query("""
            SELECT new com.ptit.thesis.smartrecruit.dto.response.CandidateStatResponse( 
            (SELECT COUNT(a) FROM Application a WHERE a.candidate.id = :id), 
            (SELECT COUNT(s) FROM SavedJob s WHERE s.candidate.id = :id)) 
            FROM CandidateProfile c 
            WHERE c.id = :id
            """)
    CandidateStatResponse getCandidateStat(long id);
}
