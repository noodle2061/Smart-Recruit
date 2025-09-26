package com.ptit.thesis.smartrecruit.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.service.AuthService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> Register(@Valid @RequestBody RegisterRequest user) {
        UserResponse userResponse = authService.register(user);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
            .status(HttpStatus.CREATED.value())
            .message("User registered successfully")
            .data(userResponse)
            .build();
        
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> handleLogin(@RequestHeader("Authorization") String authToken) {
        String cleanToken = authToken.substring(7);

        UserResponse userResponse = authService.login(cleanToken);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
            .status(HttpStatus.OK.value())
            .message("User login successfully")
            .data(userResponse)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<UserResponse>> handleOAuth2Callback(
                                    @RequestHeader("Authorization") String authorization) {
        String cleanToken = authorization.substring(7); // Loại bỏ "Bearer " khỏi đầu chuỗi

        UserResponse userResponse = authService.processAuth2CallBack(cleanToken);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
            .status(HttpStatus.OK.value())
            .message("User registered successfully")
            .data(userResponse)
            .build();
            
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
