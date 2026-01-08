package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.CompanyJobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.MyJobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;

public interface JobService {
    public JobDetailResponse createAJob(PostJobRequest job, User user);
    public PostJobMetadataResponse getPostJobMetadata();
    public JobDetailResponse getJobDetail(String slug);
    Slice<JobPageResponse> searchJobsWithFilter(Pageable pageable, 
                                                String keyword, 
                                                String location, 
                                                Long categoryId, 
                                                Long minSalary, 
                                                Long maxSalary, 
                                                ExperienceLevel experienceLevel, 
                                                List<EducationLevel> educationLevels, 
                                                List<JobType> jobTypes, 
                                                Long tagId);
    Page<MyJobPageResponse> getMyJob(Pageable pageable, JobStatus jobStatus);

    Page<CompanyJobPageResponse> getJobsByCompany(Long companyId, Pageable pageable);

    List<TagDTO> getPopularTags();

    JobDetailResponse expireJob(Long jobId);

    void changeFeaturedStatus(long jobId, boolean isFeatured);
}
