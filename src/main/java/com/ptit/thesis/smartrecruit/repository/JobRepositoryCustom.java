package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.response.AppliedJobResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyJobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;

public interface JobRepositoryCustom {
    public Slice<JobPageResponse> searchJobsWithFilter(String keyword, 
                                    String location, 
                                    String category, 
                                    Long minSalary, 
                                    Long maxSalary, 
                                    ExperienceLevel experienceLevel, 
                                    List<EducationLevel> educationLevels, 
                                    List<JobType> jobTypes,
                                    Pageable pageable);
    
    public Page<AppliedJobResponse> getCandidateAppliedJobs(
            Pageable pageable, 
            String keyword,
            JobStatus status);
    
    public Page<CompanyJobPageResponse> findJobsByCompanyId(Long companyId, Long candidateId, Pageable pageable);
}
