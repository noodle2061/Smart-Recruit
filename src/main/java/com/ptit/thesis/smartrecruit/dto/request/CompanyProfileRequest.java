package com.ptit.thesis.smartrecruit.dto.request;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyProfileRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 100, message = "Company name must be between 3 and 100 characters")
    String companyName;

    @NotBlank(message = "Description is required")
    @Size(min = 6, max = 1000, message = "Description must be between 3 and 1000 characters")
    String description;
    
    OrganizationType organizationType;
    IndustryType industryType;
    CompanyTeamSize teamSize;
    String website;
    Integer foundedIn;
    String companyVision;

    List<SocialLinkDTO> socialLinks;

    String mapLocation;
    String email;
    String phone;
}
