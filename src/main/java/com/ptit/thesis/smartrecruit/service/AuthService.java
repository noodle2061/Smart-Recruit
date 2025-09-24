package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;

public interface AuthService {
    public UserResponse register(RegisterRequest request);
    public UserResponse registerWithOAuth(OAuthRegisterRequest request);
    public void testMail();
}
