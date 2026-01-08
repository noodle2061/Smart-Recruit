package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.AppliedJobResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateFavoriteJobResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobStatus;

public interface JobCandidateService {
    void applyJob(ApplyJobRequest request, User user);
    void followJob(Long jobId, User user);
    void unfollowJob(Long jobId, User user);
    Page<AppliedJobResponse> getAppliedJobs(Pageable pageable, String keyword, JobStatus status);
    Page<CandidateFavoriteJobResponse> getFavoriteJobs(Pageable pageable, String keyword, JobStatus status);
    List<Long> getFavorites();
}
