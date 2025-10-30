package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.CandidateCompanyFollowService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/candidate/save-company")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Follow company Controller", description = "Candidate theo dõi company")
public class FollowCompanyController {
    
    CandidateCompanyFollowService candidateCompanyFollowService;

    @PostMapping("/{companyId}")
    public ResponseEntity<ApiResponse<?>> followCompany(@PathVariable Long companyId,
                                 @AuthenticationPrincipal User user) {
        
        candidateCompanyFollowService.candidateFollowCompany(companyId, user);

        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Follow company successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<?>> unfollowCompany(@PathVariable Long companyId,
                                 @AuthenticationPrincipal User user) {
        
        candidateCompanyFollowService.candidateUnfollowCompany(companyId, user);

        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Unfollow company successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
