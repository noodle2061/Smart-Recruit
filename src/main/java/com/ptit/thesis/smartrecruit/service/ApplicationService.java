package com.ptit.thesis.smartrecruit.service;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.entity.User;

public interface ApplicationService {
    void uploadResume(MultipartFile file, String title, User user);
}
