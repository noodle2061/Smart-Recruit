package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;

public interface ApplicationRepositoryCustom {
    Page<ApplicationBriefResponse> findApplicationsByJobId(Long jobId, ApplicationFilterRequest filter, Pageable pageable);
}
