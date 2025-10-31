package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.response.ResumeResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Resume;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
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

    @Override
    @Transactional
    public ResumeResponse uploadResume(MultipartFile file, String title, User user) {
        log.info("Saving resume for candidate with username {}", user.getUsername());
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));

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
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
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
}
