package com.ptit.thesis.smartrecruit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationRepositoryCustom {
    boolean existsByJobAndResume(Job job, Resume resume);
    Optional<Application> findByCandidateAndJob(CandidateProfile candidate, Job job);
    boolean existsByCandidateAndJob(CandidateProfile candidate, Job job);
    List<Application> findByJobAndStatus(Job job, JobApplicationStatus status);
}
