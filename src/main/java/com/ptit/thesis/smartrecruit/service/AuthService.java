package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;

public interface AuthService {
    public UserResponse register(RegisterRequest request);
    public UserResponse processAuth2CallBack(String authorization, OAuthRegisterRequest request);
}
