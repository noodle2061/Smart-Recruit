package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.ApplyJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.AppliedJobResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateFavoriteJobResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.service.JobCandidateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("api/candidate/jobs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Job Candidate", description = "Quản lý các thao tác với jobs của ứng viên")
public class JobCandidateController {

    JobCandidateService jobCandidateService;


    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/applied-jobs")
    @Operation(summary = "Lấy danh sách các job đã applied trong candidate dashboard")
    public ResponseEntity<ApiResponse<Page<AppliedJobResponse>>> getAppliedJobs(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobStatus status
    ) {
        Page<AppliedJobResponse> entity = jobCandidateService.getAppliedJobs(pageable, keyword, status);

        ApiResponse<Page<AppliedJobResponse>> response = ApiResponse.<Page<AppliedJobResponse>>builder()
            .status(HttpStatus.OK.value())
            .message("Get applied jobs successfully")
            .data(entity)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/favorite-jobs")
    @Operation(summary = "Lấy danh sách các job đã yêu thích/theo dõi trong candidate dashboard")
    public ResponseEntity<ApiResponse<Page<CandidateFavoriteJobResponse>>> getFavoriteJobs(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobStatus status
    ) {
        Page<CandidateFavoriteJobResponse> entity = jobCandidateService.getFavoriteJobs(pageable, keyword, status);

        ApiResponse<Page<CandidateFavoriteJobResponse>> response = ApiResponse.<Page<CandidateFavoriteJobResponse>>builder()
            .status(HttpStatus.OK.value())
            .message("Get applied jobs successfully")
            .data(entity)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    /**
     * Apply for a job
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/apply")
    @Operation(summary = "Ứng viên apply một công việc")
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
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/follow/{jobId}")
    @Operation(summary = "Ứng viên theo dõi một công việc")
    public ResponseEntity<ApiResponse<?>> followJob(@PathVariable Long jobId,
                                                    @AuthenticationPrincipal User user) {
        
        jobCandidateService.followJob(jobId, user);

        ApiResponse<?> response = ApiResponse.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("Follow job successfully")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @DeleteMapping("/unfollow/{jobId}")
    @Operation(summary = "Ứng viên bỏ theo dõi một công việc")
    public ResponseEntity<ApiResponse<?>> unfollowJob(@PathVariable Long jobId,
                                                    @AuthenticationPrincipal User user) {
        
        jobCandidateService.unfollowJob(jobId, user);

        ApiResponse<?> response = ApiResponse.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("Unfollow job successfully")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
