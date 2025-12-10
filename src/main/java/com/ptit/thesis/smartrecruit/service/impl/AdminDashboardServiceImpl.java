package com.ptit.thesis.smartrecruit.service.impl;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.response.AdminDashboardStatResponse;
import com.ptit.thesis.smartrecruit.repository.BlogRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.service.AdminDashboardService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    CandidateProfileRepository candidateProfileRepository;
    JobRepository jobRepository;
    CompanyRepository companyRepository;
    BlogRepository blogRepository;

    @Override
    public AdminDashboardStatResponse getDashboardStat() {
        AdminDashboardStatResponse response = new AdminDashboardStatResponse();
        response.setTotalCandidates(candidateProfileRepository.count());
        response.setTotalCompanies(companyRepository.count());
        response.setTotalJobs(jobRepository.count());
        response.setTotalBlogs(blogRepository.count());
        return response;
    }
    
}
