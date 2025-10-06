package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;

public interface AuthService {
    public UserResponse register(RegisterRequest request);
    public UserResponse login(String authToken);
    public UserResponse processAuth2CallBack(String authorization);
}
