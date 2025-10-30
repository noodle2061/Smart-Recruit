package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.SavedJob;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    Optional<SavedJob> findByCandidateAndJob(CandidateProfile candidate, Job job);
    void deleteByCandidateAndJob(CandidateProfile candidate, Job job);
}   
