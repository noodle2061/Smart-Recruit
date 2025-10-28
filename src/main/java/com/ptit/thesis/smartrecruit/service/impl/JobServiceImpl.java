package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.JobCategory;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.SalaryType;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.JobMapper;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.JobCategoryRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.service.JobService;
import com.ptit.thesis.smartrecruit.service.S3Service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    JobMapper jobMapper;
    JobRepository jobRepository;
    CompanyRepository companyRepository;
    JobCategoryRepository jobCategoryRepository;

    S3Service s3Service;

    @Override
    @Transactional
    public JobDetailResponse createAJob(PostJobRequest job, User user) {

        if (job == null || user == null) {
            return null;
        }

        log.info("Create job: {}", job.getTitle());

        Job newJob = jobMapper.toJobEntity(job);
        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user: " + user.getId()));

        newJob.setCompany(company);
        newJob.setLocation(company.getLocation());

        Job savedJob = jobRepository.save(newJob);

        log.info("Create new job for company {} successfully.", company.getName());

        JobDetailResponse jobDetailResponse = jobMapper.toJobDetailResponse(savedJob);

        // company basic info
        CompanyBasicInfoDTO companyBasicInfoDTO = companyRepository.findBasicInfoByUser(user);

        String avatarStorageKey = companyBasicInfoDTO.getLogoUrl();
        companyBasicInfoDTO.setLogoUrl(s3Service.generatePresignedUrl(avatarStorageKey));
        jobDetailResponse.setCompany(companyBasicInfoDTO);

        return jobDetailResponse;
    }

    @Override
    public PostJobMetadataResponse getPostJobMetadata() {
        Map<String, String> salaryTypes = Arrays.stream(SalaryType.values())
                .collect(Collectors.toMap(SalaryType::name, SalaryType::getDisplayValue));

        Map<String, String> educationLevels = Arrays.stream(EducationLevel.values())
                .collect(Collectors.toMap(EducationLevel::name, EducationLevel::getDisplayValue));

        Map<String, String> experienceLevels = Arrays.stream(EducationLevel.values())
                .collect(Collectors.toMap(EducationLevel::name, EducationLevel::getDisplayValue));

        Map<String, String> jobTypes = Arrays.stream(EducationLevel.values())
                .collect(Collectors.toMap(EducationLevel::name, EducationLevel::getDisplayValue));

        List<JobCategory> jobCategoryList = jobCategoryRepository.findAll();
        Map<Long, String> jobCategories = jobCategoryList.stream().collect(Collectors.toMap(JobCategory::getId, JobCategory::getName));

        return PostJobMetadataResponse.builder()
                .salaryTypes(salaryTypes)
                .educationLevels(educationLevels)
                .experienceLevels(experienceLevels)
                .jobTypes(jobTypes)
                .jobCategories(jobCategories)
                .build();
    }

}
