package com.ptit.thesis.smartrecruit.controller.employer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.JobService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/employer")
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
    
}
