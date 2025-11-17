package com.ptit.thesis.smartrecruit.controller.candidate;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.ResumeResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RestController
@RequestMapping("/api/applications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Candidate Application Controller", description = "Candidate: Thao tác với đơn ứng tuyển cá nhân")
public class ApplicationController {

    ApplicationService applicationService;
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo một resume")
    public ResponseEntity<ApiResponse<ResumeResponse>> updateResume(@RequestPart("resume") MultipartFile resumeFile,
                                            @RequestPart("title") String title,
                                           @AuthenticationPrincipal User user) {
        ResumeResponse resume = applicationService.uploadResume(resumeFile, title, user);

        ApiResponse<ResumeResponse> response = ApiResponse.<ResumeResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Resume uploaded successfully")
                .data(resume)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/resumes")
    @Operation(summary = "Lấy danh sách resume")
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getResumes(@AuthenticationPrincipal User user) {
        List<ResumeResponse> resumes = applicationService.getAllCandidateResumes(user);

        ApiResponse<List<ResumeResponse>> response = ApiResponse.<List<ResumeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get resumes successfully")
                .data(resumes)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @DeleteMapping("/resume/{id}")
    @Operation(summary = "Xóa một resume")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id,
                                                          @AuthenticationPrincipal User user) {
        applicationService.deleteResume(id, user);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete resume successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
