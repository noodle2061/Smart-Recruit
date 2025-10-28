package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.dto.request.CandidateBasicInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateContactInfoRequest;
import com.ptit.thesis.smartrecruit.dto.request.CandidateProfileDetailRequest;
import com.ptit.thesis.smartrecruit.dto.response.CandidateProfileResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface CandidateProfileService {
    CandidateProfileResponse updateProfile(CandidateBasicInfoRequest request, User user);
    CandidateProfileResponse updateProfile(CandidateProfileDetailRequest request, User user);
    CandidateProfileResponse updateProfile(List<SocialLinkDTO> socialLinks, User user);
    CandidateProfileResponse updateProfile(CandidateContactInfoRequest request, User user);
    void uploadAvatar(MultipartFile file, User user);
    void uploadResume(MultipartFile file, String title, User user);
}
