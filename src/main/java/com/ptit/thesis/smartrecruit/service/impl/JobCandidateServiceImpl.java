package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.entity.SavedJob;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.repository.ResumeRepository;
import com.ptit.thesis.smartrecruit.repository.SavedJobRepository;
import com.ptit.thesis.smartrecruit.service.JobCandidateService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JobCandidateServiceImpl implements JobCandidateService {

    CandidateProfileRepository candidateProfileRepository;
    ResumeRepository resumeRepository;
    ApplicationRepository applicationRepository;
    JobRepository jobRepository;
    SavedJobRepository savedJobRepository;

    @Override
    @Transactional
    public void applyJob(ApplyJobRequest request, User user) {

        log.info("Apply job from candidate " + user.getUsername() + " to Job with ID " + request.getJobId());
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                                .orElseThrow(() -> new EntityNotFoundException("Can not found candidate profile with username: " + user.getUsername()));

        Resume resume = resumeRepository.findByIdAndCandidate(request.getResumeId(), candidate)
                                .orElseThrow(() -> new EntityNotFoundException("Can not found resume with ID: " + request.getResumeId() + " that belong to this candidate"));
        
        Job job = jobRepository.findAvailableJobById(request.getJobId())
                                .orElseThrow(() -> new EntityNotFoundException("Can not found job with ID: " + request.getJobId()));

        // xử lý trường hợp nộp 2 lần -> tìm theo candidate + job => đổi resume
        Application application = applicationRepository.findByCandidateAndJob(candidate, job)
                    .orElseGet(() -> {
                        return new Application();
                    });
        if (application.getId() == null) {
            application.setCandidate(candidate);
            application.setJob(job);
            application.setResume(resume);
            application.setStatus(JobApplicationStatus.SUBMITTED);
            applicationRepository.save(application);
        } else {
            application.setResume(resume);
            application.setStatus(JobApplicationStatus.SUBMITTED);
            applicationRepository.save(application);
        }
        log.info("Apply job successfully.");
    }

    @Override
    @Transactional
    public void followJob(Long jobId, User user) {
        log.info("Following Job: From candidate with ID: " + user.getId() + " to Job with ID " + jobId);
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Can not found candidate with ID: " + user.getId()));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Can not found job with ID: " + jobId));

        Optional<SavedJob> savedJobOpt = savedJobRepository.findByCandidateAndJob(candidate, job);

        if (savedJobOpt.isEmpty()) {
            SavedJob savedJob = new SavedJob();
            savedJob.setCandidate(candidate);
            savedJob.setJob(job);
            savedJobRepository.save(savedJob);
            log.info("Following Job successfully.");
        } else {
            log.warn("Candidate with ID: " + user.getId() + " is already following Job with ID: " + jobId);
        }
        
        
    }

    @Override
    @Transactional
    public void unfollowJob(Long jobId, User user) {
        log.info("Unfollowing Job: From candidate with ID: " + user.getId() + " to Job with ID " + jobId);
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Can not found candidate with ID: " + user.getId()));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Can not found job with ID: " + jobId));
        savedJobRepository.deleteByCandidateAndJob(candidate, job);
        log.info("Unfollowing Job successfully.");
    }
    
}
