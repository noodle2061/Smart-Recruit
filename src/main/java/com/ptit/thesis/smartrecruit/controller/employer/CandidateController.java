package com.ptit.thesis.smartrecruit.controller.employer;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.dto.response.PageResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.service.CandidateCompanyFollowService;
import com.ptit.thesis.smartrecruit.service.CandidateProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/employer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Employer Candidate Controller", description = "Employer: Các thao tác liên quan đến candidate của employer")
public class CandidateController {

    CandidateProfileService candidateProfileService;
    CandidateCompanyFollowService savedCandidateService;
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/candidates")
    @Operation(summary = "Lấy dữ liệu phân trang tất cả candidate, có filter")
    public ResponseEntity<ApiResponse<Page<CandidatePageResponse>>> searchCandidates(
                                                            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam(required = false) String location,
                                                            @RequestParam(required = false) String category,
                                                            @RequestParam(required = false) ExperienceLevel experienceLevel,
                                                            @RequestParam(required = false) List<EducationLevel> educationLevels,
                                                            @RequestParam(required = false) Gender gender) {

        Page<CandidatePageResponse> response = candidateProfileService.getAllCandidates(pageable, keyword, location, category, experienceLevel, educationLevels, gender);

        ApiResponse<Page<CandidatePageResponse>> apiResponse = ApiResponse.<Page<CandidatePageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidates successfully")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/saved-candidates")
    @Operation(summary = "Lấy dữ liệu phân trang tất cả ứng viên đã lưu, KHÔNG có filter")
    public ResponseEntity<ApiResponse<List<CandidatePageResponse>>> getAllSavedCandidates(
                                                            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable) {

        Page<CandidatePageResponse> response = candidateProfileService.getAllSavedCandidates(pageable);

        ApiResponse<List<CandidatePageResponse>> apiResponse = ApiResponse.<List<CandidatePageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidates successfully")
                .data(response.getContent())
                .meta(PageResponse.of(response))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/candidate-detail/{candidateId}")
    @Operation(summary = "Lấy chi tiết thông tin của một candidate")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> getCandidateDetail(@PathVariable Long candidateId) {

        CandidateProfileResponse response = candidateProfileService.getCandidateDetail(candidateId);
        ApiResponse<CandidateProfileResponse> apiResponse = ApiResponse.<CandidateProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidate detail successfully")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/saved-candidates/{candidateId}")
    @Operation(summary = "Lưu một candidate")
    public ResponseEntity<ApiResponse<?>> saveCandidate(@PathVariable Long candidateId,
                                                        @AuthenticationPrincipal User user) {
        
        savedCandidateService.companySaveCandidate(candidateId, user);

        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Saved candidate successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/saved-candidates/{candidateId}")
    @Operation(summary = "Hủy lưu một candidate")
    public ResponseEntity<ApiResponse<?>> unsaveCandidate(@PathVariable Long candidateId,
                                                          @AuthenticationPrincipal User user) {

        savedCandidateService.companyUnsaveCandidate(candidateId, user);

        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Unsaved candidate successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
