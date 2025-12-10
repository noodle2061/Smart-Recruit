package com.ptit.thesis.smartrecruit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.AdminCompanyResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanySetupMetadata;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;

public interface CompanyService {
    public CompanyProfileResponse createOrUpdateCompanyProfile(User user, CompanyProfileRequest request, MultipartFile logo, MultipartFile banner);
    public CompanySetupMetadata getCompanySetupMetadata();
    public CompanyProfileResponse getCompanyDetails(Long companyId);
    public CompanyProfileResponse getCompanyDetails(User user);
    public Page<CompanyPageResponse> searchCompanies(Pageable pageable, String keyword, String location, OrganizationType organizationType, IndustryType industryType, CompanyTeamSize teamSize, Integer foundedIn);
    public Page<AdminCompanyResponse> getCompaniesForAdmin(String email, Pageable pageable);
    public void activateCompany(Long companyId);
    public void deactivateCompany(Long companyId);
}
