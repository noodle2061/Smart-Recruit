package com.ptit.thesis.smartrecruit.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Location;
import com.ptit.thesis.smartrecruit.entity.SocialLink;
import com.ptit.thesis.smartrecruit.enums.LinkableType;
import com.ptit.thesis.smartrecruit.repository.LocationRepository;
import com.ptit.thesis.smartrecruit.repository.SocialLinkRepository;
import com.ptit.thesis.smartrecruit.service.S3Service;

import lombok.extern.slf4j.Slf4j;

@Mapper(componentModel = "spring", uses = { LocationMapper.class,
        SocialLinkMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Slf4j
public abstract class CompanyMapper {

    @Autowired
    protected S3Service s3Service;

    @Autowired
    protected SocialLinkRepository socialLinkRepository;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected SocialLinkMapper socialLinkMapper;

    @Autowired
    protected LocationMapper locationMapper;

    @Mapping(target = "logoUrl", ignore = true) // aftermapping
    @Mapping(target = "bannerUrl", ignore = true) // aftermapping
    @Mapping(target = "socialLinks", ignore = true) // aftermapping
    @Mapping(target = "location", source = "location")
    public abstract CompanyProfileResponse toCompanyProfileResponse(Company company);

    // dto không có id, user, jobs, applicationStatusColumns, createdAt, updatedAt
    // các trường tự động map: name, description, website, phone, email
    // social link xử lý ở service sau khi đã lưu company-> có id company
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "logoUrl", ignore = true) // xử lý ở service
    @Mapping(target = "bannerUrl", ignore = true) // xử lý ở service
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    @Mapping(target = "applicationStatusColumns", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "location", ignore = true) // xử lý ở aftermapping
    public abstract void updateCompanyEntity(CompanyProfileRequest companyProfileRequest, @MappingTarget Company company);

    @AfterMapping
    protected void enrichDto(Company company, @MappingTarget CompanyProfileResponse response) {
        response.setLogoUrl(s3Service.generatePresignedUrl(company.getLogoUrl()));
        response.setBannerUrl(s3Service.generatePresignedUrl(company.getBannerUrl()));

        if (company.getId() != null) {
            List<SocialLink> socialLinks = socialLinkRepository.findByLinkableIdAndLinkableType(company.getId(),
                    LinkableType.COMPANY);
            if (socialLinks != null && socialLinks.size() > 0) {
                response.setSocialLinks(socialLinkMapper.toSocialLinkDTOs(socialLinks));
            } else {
                response.setSocialLinks(null);
            }
        }
    }

    @AfterMapping
    protected void enrichEntity(CompanyProfileRequest companyProfileRequest, @MappingTarget Company company) {
        // xử lý location, kiểm tra đã có trong database chưa, nếu chưa thì thêm với locationRepository
        Location companyLocation = company.getLocation();
        if (companyLocation == null && companyProfileRequest.getLocation() != null) {
            if (!locationRepository.existsByLatitudeAndLongitude(companyProfileRequest.getLocation().getLatitude(),
                    companyProfileRequest.getLocation().getLongitude())) {
                LocationDTO locationDTO = companyProfileRequest.getLocation();
                companyLocation.setCountry(locationDTO.getCountry());
                companyLocation.setProvinceCity(locationDTO.getProvinceCity());
                companyLocation.setCommune(locationDTO.getCommune());
                companyLocation.setLatitude(locationDTO.getLatitude());
                companyLocation.setLongitude(locationDTO.getLongitude());
                companyLocation = locationRepository.save(companyLocation);
            }
            company.setLocation(companyLocation);
        }
    }
}
