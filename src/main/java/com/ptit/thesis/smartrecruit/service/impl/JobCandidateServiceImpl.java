package com.ptit.thesis.smartrecruit.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.message.CvProcessingMessage;
import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.AppliedJobResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateFavoriteJobResponse;
import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.entity.SavedJob;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.repository.ResumeRepository;
import com.ptit.thesis.smartrecruit.repository.SavedJobRepository;
import com.ptit.thesis.smartrecruit.service.JobCandidateService;
import com.ptit.thesis.smartrecruit.service.RedisService;
import com.ptit.thesis.smartrecruit.service.S3Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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

    S3Service s3Service;
    RedisService redisService;

    RabbitTemplate rabbitTemplate;

    @NonFinal
    @Value("${rabbitmq.exchange.internal}")
    String internalExchange;

    @NonFinal
    @Value("${rabbitmq.routing-key.cv-upload}")
    String cvUploadRoutingKey;

    @Override
    @Transactional
    public void applyJob(ApplyJobRequest request, User user) {

        log.info("Apply job from candidate " + user.getUsername() + " to Job with ID " + request.getJobId());
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                                .orElseThrow(() -> new EntityNotFoundException("Can not found candidate profile with username: " + user.getUsername()));

        Resume resume = resumeRepository.findByIdAndCandidate(request.getResumeId(), candidate)
                                .orElseThrow(() -> new EntityNotFoundException("Can not found resume with ID: " + request.getResumeId() + " that belong to this candidate"));

        if (resume.getCandidate().getId() != candidate.getId()) {
            throw new AccessDeniedException("Can not found resume with ID: " + request.getResumeId() + " that belong to this candidate");
        }
        
        Job job = jobRepository.findAvailableJobById(request.getJobId())
                                .orElseThrow(() -> new EntityNotFoundException("Can not found job with ID: " + request.getJobId()));

        // xử lý trường hợp nộp 2 lần
        //  -> check time to live ở redis xem trong 10p trước đã nộp lần nào chưa, nếu nộp rồi thì không cho nộp 2 lần trong 10 phút
        //  -> nếu nộp rồi thi tăng data version trong application
        //  -> 
        Application application = applicationRepository.findByCandidateAndJob(candidate, job)
                    .orElseGet(() -> {
                        return new Application();
                    });
        
        if (application.getId() == null) { // đơn mới
            application.setCandidate(candidate);
            application.setJob(job);
            application.setDataVersion(1);
        } else { // đã từng nộp
            if (application.getStatus() == JobApplicationStatus.REJECTED) {
                throw new RuntimeException("Your application has been rejected or job has been expired.");
            }
            if (redisService.isApplyRateLimit(application.getId())) {
                throw new RuntimeException("You have already applied to this job within the last 10 minutes.");
            }
            application.setDataVersion(application.getDataVersion() + 1);
        }
        
        redisService.setApplyRateLimit(application.getId());

        application.setResume(resume);
        application.setStatus(JobApplicationStatus.PROCESSING);
        
        Application savedApplication = applicationRepository.save(application);
        
        try {
            String fileUrl = s3Service.generatePresignedUrl(resume.getStorageKey(), Duration.ofDays(7));
            
            CvProcessingMessage message = CvProcessingMessage.builder()
                    .applicationId(savedApplication.getId())
                    .fileUrl(fileUrl)
                    .version(savedApplication.getDataVersion())
                    .jobTitle(job.getTitle())
                    .jobDescription(job.getDescription())
                    .jobResponsibilities(job.getResponsibilities())
                    .educationLevel(job.getEducationLevel() != null ? job.getEducationLevel().name() : null)
                    .experienceLevel(job.getExperienceLevel() != null ? job.getExperienceLevel().name() : null)
                    .build();
            
            rabbitTemplate.convertAndSend(internalExchange, cvUploadRoutingKey, message);
            log.info("Sent CV scoring request to RabbitMQ for Application ID: {}", savedApplication.getId());
        } catch (Exception e) {
            log.error("Failed to send RabbitMQ message for Application ID: {}. Error: {}", savedApplication.getId(), e.getMessage());
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

    @Override
    public Page<AppliedJobResponse> getAppliedJobs(Pageable pageable, String keyword, JobStatus status) {
        Page<AppliedJobResponse> appliedJobs = jobRepository.getCandidateAppliedJobs(pageable, keyword, status).map(
            jobResponse -> {
                jobResponse.setCompanyLogoUrl(s3Service.generatePresignedUrl(jobResponse.getCompanyLogoUrl()));
                return jobResponse;
            }
        );

        return appliedJobs;
    }

    @Override
    public Page<CandidateFavoriteJobResponse> getFavoriteJobs(Pageable pageable, String keyword,
            JobStatus status) {
        Page<CandidateFavoriteJobResponse> favoriteJobs = jobRepository.getCandidateFavoriteJobs(pageable, keyword, status).map(
            jobResponse -> {
                jobResponse.setCompanyLogoUrl(s3Service.generatePresignedUrl(jobResponse.getCompanyLogoUrl()));
                return jobResponse;
            }
        );

        return favoriteJobs;
    }

    @Override
    public List<Long> getFavorites() {
        return jobRepository.getCandidateFavoriteJobIds();
    }
}