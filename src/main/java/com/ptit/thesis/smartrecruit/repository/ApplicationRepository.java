package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobAndResume(Job job, Resume resume);
    Optional<Application> findByCandidateAndJob(CandidateProfile candidate, Job job);
}
