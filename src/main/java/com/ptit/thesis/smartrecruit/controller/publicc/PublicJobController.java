package com.ptit.thesis.smartrecruit.controller.publicc;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.service.JobService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("/api") // không để api /public vì còn để phân biệt người bình thường với candidate để điều chỉnh dto
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PublicJobController {
    
    JobService jobService;

    @GetMapping("/job/{slug}")
    public ResponseEntity<ApiResponse<JobDetailResponse>> getJobDetailBySlug(@PathVariable String slug) {
        JobDetailResponse jobDetailResponse = jobService.getJobDetail(slug);

        ApiResponse<JobDetailResponse> response = ApiResponse.<JobDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get job detail successfully")
                .data(jobDetailResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Slice<JobPageResponse>>> searchJobsWithFilter(
                                                            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) String location,
                                                            @RequestParam(required = false) String category,
                                                            @RequestParam(required = false) Long minSalary,
                                                            @RequestParam(required = false) Long maxSalary,
                                                            @RequestParam(required = false) ExperienceLevel experienceLevel,
                                                            @RequestParam(required = false) List<EducationLevel> educationLevels,
                                                            @RequestParam(required = false) List<JobType> jobTypes
    ) {
        
        Slice<JobPageResponse> jobPageResponses = jobService.searchJobsWithFilter(pageable, keyword, location, category, minSalary, maxSalary, experienceLevel, educationLevels, jobTypes);
        ApiResponse<Slice<JobPageResponse>> response = ApiResponse.<Slice<JobPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get jobs successfully")
                .data(jobPageResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
}
