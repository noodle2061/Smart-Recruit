package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.JobCategory;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.SalaryType;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.JobMapper;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.JobCategoryRepository;
import com.ptit.thesis.smartrecruit.repository.JobRepository;
import com.ptit.thesis.smartrecruit.repository.SavedJobRepository;
import com.ptit.thesis.smartrecruit.service.JobService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.Constraint;

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
    SavedJobRepository savedJobRepository;
    ApplicationRepository applicationRepository;
    CandidateProfileRepository candidateProfileRepository;

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

        JobDetailResponse jobDetailResponse = getJobDetailResponseFromEntity(savedJob, company);

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

    @Override
    @Transactional
    public JobDetailResponse getJobDetail(String slug) {
        Job job = jobRepository.findAvailableJobWithCompany(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found for slug: " + slug));
        Company ownerCompany = job.getCompany();

        JobDetailResponse response = getJobDetailResponseFromEntity(job, ownerCompany);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) { // không phải khách vãng lai
            User user = (User) principal;
            String roleUpper = user.getRole().getName();
            if (roleUpper.equals(Constraint.CANDIDATE_ROLE)) { // role candidate thì set thêm trường isFavorite và isApplied
                CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("Candidate not found for user: " + user.getId()));
                response.setIsFavorite(savedJobRepository.existsByCandidateAndJob(candidate, job));
                response.setIsApplied(applicationRepository.existsByCandidateAndJob(candidate, job));
            }
        }

        return response;
    }

    public JobDetailResponse getJobDetailResponseFromEntity(Job job, Company company) {
            JobDetailResponse jobDetailResponse = jobMapper.toJobDetailResponse(job);

        // company basic info
        CompanyBasicInfoDTO companyBasicInfoDTO = companyRepository.findBasicInfoById(company.getId());

        String avatarStorageKey = companyBasicInfoDTO.getLogoUrl();
        companyBasicInfoDTO.setLogoUrl(s3Service.generatePresignedUrl(avatarStorageKey));
        jobDetailResponse.setCompany(companyBasicInfoDTO);

        return jobDetailResponse;
    }

}
