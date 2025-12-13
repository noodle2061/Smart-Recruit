package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.request.ApplicationStatusUpdateRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;
import com.ptit.thesis.smartrecruit.dto.response.ResumeResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface ApplicationService {
    ResumeResponse uploadResume(MultipartFile file, String title, User user);
    List<ResumeResponse> getAllCandidateResumes(User user);
    Page<ApplicationBriefResponse> getApplicationsForJob(Pageable pageable, ApplicationFilterRequest filter, Long jobId);
    void deleteResume(Long resumeId, User user);
    ResumeResponse updateResume(Long id, MultipartFile resumeFile, String title, User user);
    void updateApplicationScore(Long applicationId, Double score);
    void updateApplicationStatus(Long jobId, Long applicationId, ApplicationStatusUpdateRequest request);
}
