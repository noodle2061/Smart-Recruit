package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.entity.User;

public interface JobCandidateService {
    void applyJob(ApplyJobRequest request, User user);
}
