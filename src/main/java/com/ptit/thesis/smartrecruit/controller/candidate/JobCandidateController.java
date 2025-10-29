package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.JobCandidateService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/candidate/jobs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Job Candidate", description = "Quản lý các thao tác với jobs của ứng viên")
public class JobCandidateController {

    JobCandidateService jobCandidateService;
    
    /**
     * Apply for a job
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<?>> applyJob(@Valid @RequestBody ApplyJobRequest request,
                                                    @AuthenticationPrincipal User user) {
        
        jobCandidateService.applyJob(request, user);
        
        ApiResponse<?> response = ApiResponse.<Object>builder()
                .status(HttpStatus.CREATED.value())
                .message("Apply job successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
