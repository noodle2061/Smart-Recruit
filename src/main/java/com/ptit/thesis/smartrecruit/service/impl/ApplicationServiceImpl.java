package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;
import com.ptit.thesis.smartrecruit.dto.response.ResumeResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.repository.ResumeRepository;
import com.ptit.thesis.smartrecruit.service.ApplicationService;
import com.ptit.thesis.smartrecruit.service.S3Service;

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
public class ApplicationServiceImpl implements ApplicationService {

    S3Service s3Service;
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
    public Page<ApplicationBriefResponse> getApplicationsForJob(Pageable pageable, ApplicationFilterRequest filter,
            Long jobId) {
        User existUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Company company = companyRepository.findByUser(existUser)
                .orElseThrow(() -> new EntityNotFoundException("Company not found for user: " + existUser.getId()));
        Job existJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found for id: " + jobId));
        if (company.getId() != existJob.getCompany().getId()) {
            throw new IllegalArgumentException("Job with id " + jobId + " does not belong to company " + company.getName());
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
}
