package com.ptit.thesis.smartrecruit.controller.employer;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.ApplicationFilterRequest;
import com.ptit.thesis.smartrecruit.dto.request.ApplicationStatusUpdateRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.ApplicationBriefResponse;
import com.ptit.thesis.smartrecruit.dto.response.PageResponse;
import com.ptit.thesis.smartrecruit.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/employer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Employer Application", description = "Employer: Các thao tác với đơn ứng tuyển của công ty")
public class EmployerApplicationController {

    ApplicationService applicationService;
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/job/{jobId}/applications")
    @Operation(summary = "Lấy danh sách đơn ứng tuyển của một job có hỗ trợ phân trang filter")
    public ResponseEntity<ApiResponse<List<ApplicationBriefResponse>>> getApplicationsForJob(
        @RequestParam Long jobId,
        @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
        @ParameterObject ApplicationFilterRequest filter) {
        Page<ApplicationBriefResponse> applications = applicationService.getApplicationsForJob(pageable, filter, jobId);
        ApiResponse<List<ApplicationBriefResponse>> response = ApiResponse.<List<ApplicationBriefResponse>>builder()
            .status(HttpStatus.OK.value())
            .message("Get applications successfully")
            .data(applications.getContent())
            .meta(PageResponse.of(applications))
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/job/{jobId}/application/{applicationId}/status")
    @Operation(summary = "Cập nhật trạng thái đơn ứng tuyển")
    public ResponseEntity<ApiResponse<Void>> updateApplicationStatus(@PathVariable Long jobId, 
        @PathVariable Long applicationId,
        @RequestBody ApplicationStatusUpdateRequest request) {
        applicationService.updateApplicationStatus(jobId, applicationId, request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Update application status successful")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
