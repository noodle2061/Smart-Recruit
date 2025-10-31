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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.service.CandidateProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Candidate", description = "Employer: Get Candidate Infomation APIs")
public class CandidateController {

    CandidateProfileService candidateProfileService;
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/candidates")
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
    @GetMapping("/candidate-detail/{candidateId}")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> searchCandidateDetail(@PathVariable Long candidateId) {

        CandidateProfileResponse response = candidateProfileService.getCandidateDetail(candidateId);
        ApiResponse<CandidateProfileResponse> apiResponse = ApiResponse.<CandidateProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get candidate detail successfully")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
