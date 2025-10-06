package com.ptit.thesis.smartrecruit.controller.employer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.entity.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("api/employer/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyController {
    
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCompanyInfo(@AuthenticationPrincipal User user) {
        // chưa xử lý
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    

    @PostMapping("/")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
