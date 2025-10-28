package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public void uploadResume(MultipartFile file, String title, User user) {
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
            
            resumeRepository.save(resume);
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
}
