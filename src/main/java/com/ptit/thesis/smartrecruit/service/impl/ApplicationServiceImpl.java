package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.message.CvResultScoreMessage;
import com.ptit.thesis.smartrecruit.dto.message.CvResultScoreMessage.MatchDataDTO;
import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.request.ApplicationStatusUpdateRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;
import com.ptit.thesis.smartrecruit.dto.response.ResumeResponse;
import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;
import com.ptit.thesis.smartrecruit.enums.NotificationType;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.repository.ResumeRepository;
import com.ptit.thesis.smartrecruit.service.ApplicationService;
import com.ptit.thesis.smartrecruit.service.NotificationService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.StringUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    S3Service s3Service;
    NotificationService notificationService;

    CandidateProfileRepository candidateProfileRepository;
    ResumeRepository resumeRepository;
    CompanyRepository companyRepository;
    JobRepository jobRepository;
    ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public ResumeResponse uploadResume(MultipartFile file, String title, User user) {
        log.info("Saving resume for candidate with username {}", user.getUsername());
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));

        if (!checklegalCandidate(candidateProfile)) {
            throw new IllegalArgumentException("You must setup your candidate profile first.");
        }

        String newResumeKey = null;
        try {
            newResumeKey = s3Service.uploadFile(file, "candidate/resume/");
            Resume resume = new Resume();
            resume.setStorageKey(newResumeKey);
            resume.setSize((file.getSize() / (1024.0f * 1024.0f)));
            resume.setTitle(title);
            resume.setCandidate(candidateProfile);

            Resume savedResume = resumeRepository.save(resume);

            ResumeResponse resumeResponse = toDTO(savedResume);

            return resumeResponse;
        } catch (IOException e) {
            throw new S3ErrorException("Error uploading resume to the S3: " + e.getMessage());
        } catch (Exception e) {
            if (newResumeKey != null) {
                s3Service.deleteFileByKey(newResumeKey);
            }
            log.error("Error uploading resume to the database: {}", e.getMessage());
            throw new S3ErrorException("Error uploading resume to the database: " + e.getMessage());
        }
    }

    @Override
    public List<ResumeResponse> getAllCandidateResumes(User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        return resumeRepository.findAllByCandidate(candidateProfile).stream().map(resume -> toDTO(resume)).toList();
    }

    @Override
    @Transactional
    public ResumeResponse updateResume(Long id, MultipartFile resumeFile, String title, User user) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found for id: " + id));

        if (resume.getCandidate().getUser().getId() != user.getId()) {
            throw new IllegalArgumentException(
                    "Resume with id " + id + " does not belong to user " + user.getUsername());
        }

        resume.setTitle(StringUtil.hasText(title) ? title : resume.getTitle());

        if (resumeFile != null && !resumeFile.isEmpty()) {
            String newResumeKey = null;
            try {
                newResumeKey = s3Service.uploadFile(resumeFile, "candidate/resume/");
                s3Service.deleteFileByKey(resume.getStorageKey());
                resume.setStorageKey(newResumeKey);
                resume.setSize((resumeFile.getSize() / (1024.0f * 1024.0f)));
            } catch (IOException e) {
                throw new S3ErrorException("Error uploading resume to the S3: " + e.getMessage());
            }
        }

        Resume savedResume = resumeRepository.save(resume);
        return toDTO(savedResume);
    }

    @Override
    @Transactional
    public Page<ApplicationBriefResponse> getApplicationsForJob(Pageable pageable, ApplicationFilterRequest filter,
            Long jobId) {
        User existUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Company company = companyRepository.findByUser(existUser)
                .orElseThrow(() -> new EntityNotFoundException("Company not found for user: " + existUser.getId()));
        Job existJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found for id: " + jobId));
        if (company.getId() != existJob.getCompany().getId()) {
            throw new IllegalArgumentException(
                    "Job with id " + jobId + " does not belong to company " + company.getName());
        }
        return applicationRepository.findApplicationsByJobId(jobId, filter, pageable)
                .map(dto -> {
                    dto.setCandidateAvatarUrl(s3Service.generatePresignedUrl(dto.getCandidateAvatarUrl()));
                    dto.setResumeUrl(s3Service.generatePresignedUrl(dto.getResumeUrl()));
                    return dto;
                });
    }

    private boolean checklegalCandidate(CandidateProfile candidate) {
        return (candidate.getGender() != null && candidate.getDateOfBirth() != null && candidate.getPhone() != null
                && candidate.getEducationLevel() != null && candidate.getExperienceLevel() != null);
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId, User user) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found for id: " + resumeId));

        if (resume.getCandidate().getUser().getId() != user.getId()) {
            throw new IllegalArgumentException(
                    "Resume with id " + resumeId + " does not belong to user " + user.getUsername());
        }

        try {
            s3Service.deleteFileByKey(resume.getStorageKey());
        } catch (S3Exception e) {
            throw new S3ErrorException("Error deleting resume from S3: " + e.getMessage());
        }
        resumeRepository.delete(resume);
    }

    public ResumeResponse toDTO(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .size(resume.getSize())
                .url(s3Service.generatePresignedUrl(resume.getStorageKey()))
                .build();
    }

    @Override
    @Transactional
    public void updateApplicationScore(CvResultScoreMessage message) {

        Long applicationId = message.getApplicationId();
        MatchDataDTO matchData = message.getData();
        Double score = matchData.getMatchScore();
        Application application = null;
        Optional<Application> applicationOpt = applicationRepository.findById(message.getApplicationId());
        if (applicationOpt.isPresent()) {
            application = applicationOpt.get();

            if (application.getDataVersion() != message.getVersion()) {
                return; // không phải version cuối thì không cập nhật gì cả mà bỏ qua
            }

            if (message.getIsSuccess()) {
                application.setScore(score);
                application.setBreakdownScore(matchData.getBreakdown().toString());
                applicationRepository.save(application);
            }
        } else {
            log.error("Error updating application score: Application not found for id: " + message.getApplicationId());
            return;
        }

        User candidateUser = application.getCandidate().getUser();
        Company company = application.getJob().getCompany();

        if (message.getIsSuccess() == false) {
            notificationService.pushNotification(
                    null,
                    candidateUser,
                    "Hồ sơ ứng tuyển của bạn cho vị trí " + application.getJob().getTitle() + " của "
                            + company.getName() + " không hợp lệ: " + message.getError(),
                    NotificationType.JOB_APPLICATION,
                    applicationId);
                    
            application.setScore(0.0); // đánh dấu là đã chấm và không hợp lệ
            applicationRepository.save(application);
            return;
        }

        notificationService.pushNotification(
                null,
                candidateUser,
                "Hồ sơ ứng tuyển của bạn đạt " + score + " điểm cho công việc " + application.getJob().getTitle()
                        + " của " + company.getName(),
                NotificationType.JOB_APPLICATION,
                application.getId());
    }

    @Override
    public void updateApplicationStatus(Long jobId, Long applicationId, ApplicationStatusUpdateRequest request) {
        JobApplicationStatus newStatus = request.getStatus();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found for id: " + applicationId));
        Job job = application.getJob();
        if (job.getId() != jobId) {
            throw new IllegalArgumentException(
                    "Application with id " + applicationId + " does not belong to job " + jobId);
        }

        application.setStatus(newStatus);
        applicationRepository.save(application);

        String content = "";
        if (newStatus == JobApplicationStatus.REJECTED) {
            content = String.format("Rất tiếc, hồ sơ của bạn cho vị trí %s chưa phù hợp tại thời điểm này. ",
                    job.getTitle());
        } else if (newStatus == JobApplicationStatus.ACCEPTED) {
            content = String.format("Chúc mừng, hồ sơ của bạn tại vị trí %s đã được chấp nhận.", job.getTitle());
        }
        User candidateUser = application.getCandidate().getUser();
        notificationService.pushNotification(
                null,
                candidateUser,
                content,
                NotificationType.JOB_APPLICATION,
                job.getId());

    }
}
