package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;

public interface RankingService {
    public List<JobPageResponse> getHotJobs(int limit);
    public List<CompanyPageResponse> getHotCompanies(int limit);
}
