package com.ptit.thesis.smartrecruit.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.service.S3Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public abstract class CompanyMapper {

    S3Service s3Service;

    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "bannerUrl", ignore = true)
    @Mapping(target = "socialLinks", ignore = true)
    @Mapping(target = "mapLocation", ignore = true)
    public abstract CompanyProfileResponse toCompanyProfileResponse(Company company);

    public abstract Company toCompanyEntity(CompanyProfileRequest companyProfileRequest);

    @AfterMapping 
    protected void dto(Company company, @MappingTarget CompanyProfileResponse response) {
        response.setLogoUrl(s3Service.generatePresignedUrl(company.getLogoUrl()));
        response.setBannerUrl(s3Service.generatePresignedUrl(company.getCoverPhotoUrl()));
    }
}
