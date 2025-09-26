package com.ptit.thesis.smartrecruit.service;

import org.springframework.security.core.Authentication;

import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;

public interface AuthService {
    public UserResponse register(RegisterRequest request);
    public UserResponse login(Authentication authentication);
    public UserResponse processAuth2CallBack(String authorization, OAuthRegisterRequest request);
    public UserResponse processAuth2CallBack(String authorization);
}
