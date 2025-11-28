package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanySetupMetadata;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.SocialLink;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.LinkableType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
import com.ptit.thesis.smartrecruit.enums.PlatformName;
import com.ptit.thesis.smartrecruit.exception.InvalidFieldException;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.mapper.CompanyMapper;
import com.ptit.thesis.smartrecruit.mapper.SocialLinkMapper;
import com.ptit.thesis.smartrecruit.repository.CandidateCompanyRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepositoryCustom;
import com.ptit.thesis.smartrecruit.repository.SocialLinkRepository;
import com.ptit.thesis.smartrecruit.service.CompanyService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.Constant;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    S3Service s3Service;
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    SocialLinkRepository socialLinkRepository;
    CandidateCompanyRepository candidateCompanyRepository;
    CompanyRepositoryCustom companyRepositoryCustom;

    SocialLinkMapper socialLinkMapper;

    @Override
    @Transactional
    public CompanyProfileResponse createOrUpdateCompanyProfile(User user, CompanyProfileRequest request, MultipartFile logo,
            MultipartFile banner) {

        log.info("Starting to save or update company info for user: {}", user.getUsername());

        Company company = companyRepository.findByUser(user).orElse(new Company());

        if (isNewCompany(company) && isCompanyNameExist(request.getName())) {
            throw new InvalidFieldException("Company name is already in use.");
        }

        // lưu lên S3
        try {
            if (logo != null && !logo.isEmpty()) {
                String oldLogoKey = company.getLogoUrl();
                String newLogoKey = s3Service.uploadFile(logo, "companies/logos");
                company.setLogoUrl(newLogoKey);
                if (oldLogoKey != null) {
                    s3Service.deleteFileByKey(oldLogoKey);
                }
            }
            if (banner != null && !banner.isEmpty()) {
                String oldBannerKey = company.getBannerUrl();
                String newBannerKey = s3Service.uploadFile(banner, "companies/banners");
                company.setBannerUrl(newBannerKey);
                if (oldBannerKey != null) {
                    s3Service.deleteFileByKey(oldBannerKey);
                }
            }
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new S3ErrorException("Error uploading file to S3: " + e.getMessage());
        }

        if (isNewCompany(company)) {
            company.setUser(user);
        }

        companyMapper.updateCompanyEntity(request, company);

        try {
            Company savedCompany = companyRepository.save(company);
            log.info("Successfully saved or updated company info for user: {}", user.getUsername());

            // lưu social link
            saveSocialLink(request, savedCompany);
            // trả về response
            CompanyProfileResponse companyResponse = companyMapper.toCompanyProfileResponse(savedCompany);
            return companyResponse;
        } catch (Exception e) {
            // xóa file trên s3 tránh mồ côi file khi xảy ra lỗi lưu db
            s3Service.deleteFileByKey(company.getLogoUrl());
            s3Service.deleteFileByKey(company.getBannerUrl());

            log.error("Error saving or updating company info for user: {}", user.getUsername(), e);
            throw new RuntimeException("Error saving or updating company info for user: " + user.getUsername(), e);
        }
    }

    public boolean isNewCompany(Company company) {
        return company.getId() == null;
    }

    public boolean isCompanyNameExist(String companyName) {
        return companyRepository.existsByName(companyName);
    }

    public void saveSocialLink(CompanyProfileRequest request, Company company) {
        // xử lý socialLinks
        // xóa hết trong csdl xong rồi thêm lại danh sách
        socialLinkRepository.deleteAllByLinkableIdAndLinkableType(company.getId(), LinkableType.COMPANY);
        List<SocialLink> socialLinks = socialLinkMapper.toSocialLinks(request.getSocialLinks())
                                            .stream().map(socialLink -> {
                                                socialLink.setLinkableId(company.getId());
                                                socialLink.setLinkableType(LinkableType.COMPANY);
                                                return socialLink;
                                            }).toList();
        if (socialLinks != null && socialLinks.size() > 0) {
            socialLinkRepository.saveAll(socialLinks);
        }
    }

    @Override
    public CompanySetupMetadata getCompanySetupMetadata() {
        Map<String, String> organizationTypes = Arrays.stream(OrganizationType.values())
                        .collect(Collectors.toMap(OrganizationType::name, OrganizationType::getDisplayValue));
        Map<String, String> industryTypes = Arrays.stream(IndustryType.values())
                        .collect(Collectors.toMap(IndustryType::name, IndustryType::getDisplayValue));
        Map<String, String> teamSizes = Arrays.stream(CompanyTeamSize.values())
                        .collect(Collectors.toMap(CompanyTeamSize::name, CompanyTeamSize::getDisplayValue));
        Map<String, String> platformNames = Arrays.stream(PlatformName.values())
                        .collect(Collectors.toMap(p -> p.name(), p -> p.getDisplayName()));

        CompanySetupMetadata metadata = CompanySetupMetadata.builder()
                        .organizationTypes(organizationTypes)
                        .industryTypes(industryTypes)
                        .teamSizes(teamSizes)
                        .platformNames(platformNames)
                        .build();
        return metadata;
    }

    @Override
    @Transactional
    public CompanyProfileResponse getCompanyDetails(Long companyId) {
        log.info("Getting company details.");
        Company company = companyRepository.findById(companyId)
                                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        CompanyProfileResponse response = companyMapper.toCompanyProfileResponse(company);
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof User) {
            log.info("User already login.");
            User user = (User) principle;
            String roleUpper = user.getRole().getName();
            if (roleUpper.equals(Constant.CANDIDATE_ROLE)) {
                response.setIsFavorite(candidateCompanyRepository.existsByCandidateAndCompany(user.getCandidateProfile(), company));
            }
        } else {
            log.info("User not login.");
        }
        log.info("Getting company details successfully.");
        return response;
    }

    @Override
    public Page<CompanyPageResponse> searchCompanies(Pageable pageable, String keyword, String location,
            OrganizationType organizationType, IndustryType industryType, CompanyTeamSize teamSize, Integer foundedIn) {
        
        Page<CompanyPageResponse> companyResponses = companyRepositoryCustom.searchCompany(
                        keyword, 
                        location, 
                        organizationType, 
                        industryType, 
                        teamSize, 
                        foundedIn, 
                        pageable).map(companyResponse -> {
                                companyResponse.setLogoUrl(s3Service.generatePresignedUrl(companyResponse.getLogoUrl()));
            return companyResponse;
        });

        return companyResponses;
    }

    @Override
    public CompanyProfileResponse getCompanyDetails(User user) {
        Company company = companyRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException("Company not found for user: " + user.getId()));
        return getCompanyDetails(company.getId());
    }
}
