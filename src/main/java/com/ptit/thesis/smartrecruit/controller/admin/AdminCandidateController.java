package com.ptit.thesis.smartrecruit.controller.admin;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.AdminCandidateResponse;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.service.CandidateProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/candidates")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Admin Candidate Controller", description = "Admin Candidate Controller")
public class AdminCandidateController {

    CandidateProfileService candidateProfileService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    @Operation(summary = "Lấy danh sách các candidate")
    public ResponseEntity<ApiResponse<Page<AdminCandidateResponse>>> getCandidates(
            @RequestParam(required = false) String email,
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable) {
        Page<AdminCandidateResponse> entity = candidateProfileService.getCandidatesForAdmin(pageable, email);
        ApiResponse<Page<AdminCandidateResponse>> response = ApiResponse.<Page<AdminCandidateResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidates successfully")
                .data(entity)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{candidateId}")
    @Operation(summary = "Lấy chi tiết thông tin của một candidate")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getCandidateDetail(@PathVariable Long candidateId) {
        CandidateProfileResponse entity = candidateProfileService.getCandidateDetail(candidateId);
        ApiResponse<CandidateProfileResponse> response = ApiResponse.<CandidateProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidate successfully")
                .data(entity)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{candidateId}/deactivate")
    @Operation(summary = "Vô hiệu hóa một tài khoản candidate")
    public ResponseEntity<ApiResponse<Void>> deactivateCandidate(@PathVariable Long candidateId) {
        candidateProfileService.deactivateCandidate(candidateId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("deactivate candidate successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{candidateId}/activate")
    @Operation(summary = "Kích hoạt lại một tài khoản candidate")
    public ResponseEntity<ApiResponse<Void>> activateCandidate(@PathVariable Long candidateId) {
        candidateProfileService.activateCandidate(candidateId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("activate candidate successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
