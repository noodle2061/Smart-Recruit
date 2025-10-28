package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface JobService {
    public JobDetailResponse createAJob(PostJobRequest job, User user);
    public PostJobMetadataResponse getPostJobMetadata();
}
