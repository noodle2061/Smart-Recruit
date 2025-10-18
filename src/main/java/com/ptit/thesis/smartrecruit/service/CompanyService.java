package com.ptit.thesis.smartrecruit.service;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface CompanyService {
    public CompanyProfileResponse createOrUpdateCompanyProfile(User user, CompanyProfileRequest request, MultipartFile logo, MultipartFile banner);
}
