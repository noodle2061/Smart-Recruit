package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.ApplicationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RestController
@RequestMapping("/api/applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ApplicationController {

    ApplicationService applicationService;
    
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping(value = "/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateResume(@RequestPart("resume") MultipartFile resumeFile,
                                            @RequestPart("title") String title,
                                           @AuthenticationPrincipal User user) {
        applicationService.uploadResume(resumeFile, title, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
