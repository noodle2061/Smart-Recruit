package com.ptit.thesis.smartrecruit.controller.candidate;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.dto.request.CandidateBasicInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateContactInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateProfileDetailRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.CandidateProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/candidate/profile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name="CandidateProfileController", description="Quản lý thông tin Candidate")
public class CandidateProfileController {

    CandidateProfileService candidateProfileService;
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @PatchMapping("/basic-info")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> updateBasicInfo(@Valid @RequestBody CandidateBasicInfoRequest request,
                                @AuthenticationPrincipal User user) {
        CandidateProfileResponse entity = candidateProfileService.updateProfile(request, user);
        
        ApiResponse<CandidateProfileResponse> response = ApiResponse.<CandidateProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Candidate profile updated successfully")
            .data(entity)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PatchMapping("/info-detail")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> updateInfoDetail(@Valid @RequestBody CandidateProfileDetailRequest request,
                                @AuthenticationPrincipal User user) {
        
        CandidateProfileResponse entity = candidateProfileService.updateProfile(request, user);
        
        ApiResponse<CandidateProfileResponse> response = ApiResponse.<CandidateProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Candidate profile updated successfully")
            .data(entity)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PatchMapping("/social-links")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> updateSocialLinks(@Valid @RequestBody List<SocialLinkDTO> socialLinks,
                                                                                    @AuthenticationPrincipal User user) {
        CandidateProfileResponse entity = candidateProfileService.updateProfile(socialLinks, user);
        
        ApiResponse<CandidateProfileResponse> response = ApiResponse.<CandidateProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Candidate profile updated successfully")
            .data(entity)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PatchMapping("/contact-info")
    public ResponseEntity<ApiResponse<CandidateProfileResponse>> updateContactInfo(@Valid @RequestBody CandidateContactInfoRequest request,
                                                                                    @AuthenticationPrincipal User user) {
        CandidateProfileResponse entity = candidateProfileService.updateProfile(request, user);
        
        ApiResponse<CandidateProfileResponse> response = ApiResponse.<CandidateProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Candidate profile updated successfully")
            .data(entity)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile avatarFile,
                                           @AuthenticationPrincipal User user) {
        candidateProfileService.uploadAvatar(avatarFile, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateResume(@RequestPart("resume") MultipartFile resumeFile,
                                            @RequestPart("title") String title,
                                           @AuthenticationPrincipal User user) {
        candidateProfileService.uploadResume(resumeFile, title, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
