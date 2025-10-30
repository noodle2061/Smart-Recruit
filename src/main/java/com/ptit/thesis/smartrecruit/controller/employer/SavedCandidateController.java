package com.ptit.thesis.smartrecruit.controller.employer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.SavedCandidateService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/employer/saved-candidates")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SavedCandidateController {

    SavedCandidateService savedCandidateService;
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/{candidateId}")
    public ResponseEntity<ApiResponse<?>> saveCandidate(@PathVariable Long candidateId,
                                                        @AuthenticationPrincipal User user) {

        savedCandidateService.saveCandidate(candidateId, user);

        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Saved candidate successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/{candidateId}")
    public ResponseEntity<ApiResponse<?>> unsaveCandidate(@PathVariable Long candidateId,
                                                          @AuthenticationPrincipal User user) {

        savedCandidateService.unsaveCandidate(candidateId, user);
        
        ApiResponse<?> response = ApiResponse.builder()
            .status(HttpStatus.OK.value())
            .message("Unsaved candidate successfully")
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
