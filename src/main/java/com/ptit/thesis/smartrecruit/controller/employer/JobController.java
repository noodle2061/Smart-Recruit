package com.ptit.thesis.smartrecruit.controller.employer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.dto.response.PostJobMetadataResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.JobService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/employer")
@Tag(name="JobController", description="Quản lý công việc")
public class JobController {

    JobService jobService;
    
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
