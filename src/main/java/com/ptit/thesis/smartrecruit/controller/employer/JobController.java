package com.ptit.thesis.smartrecruit.controller.employer;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.MyJobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.service.JobService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/employer")
@Tag(name="JobController", description="Quản lý công việc")
public class JobController {

    JobService jobService;
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("jobs")
    public ResponseEntity<ApiResponse<Page<MyJobPageResponse>>> getMyJob(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @RequestParam(required = false) JobStatus jobStatus
    ) {
        Page<MyJobPageResponse> myJobPageResponse = jobService.getMyJob(pageable, jobStatus);
        ApiResponse<Page<MyJobPageResponse>> response = ApiResponse.<Page<MyJobPageResponse>>builder()
            .status(HttpStatus.OK.value())
            .message("Get my job successfully")
            .data(myJobPageResponse)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/job")
    public ResponseEntity<ApiResponse<JobDetailResponse>> createJob(@Valid @RequestBody PostJobRequest job,
                                    @AuthenticationPrincipal User user) {
        JobDetailResponse jobDetailResponse = jobService.createAJob(job, user);

        ApiResponse<JobDetailResponse> response = ApiResponse.<JobDetailResponse>builder()
            .status(HttpStatus.CREATED.value())
            .message("Job created successfully")
            .data(jobDetailResponse)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/metadata")
    public ResponseEntity<ApiResponse<PostJobMetadataResponse>> getPostJobMetadata() {
        PostJobMetadataResponse metadata = jobService.getPostJobMetadata();
        ApiResponse<PostJobMetadataResponse> response = ApiResponse.<PostJobMetadataResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Get post job metadata successfully")
            .data(metadata)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    
    
}
