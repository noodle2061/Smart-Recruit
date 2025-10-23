package com.ptit.thesis.smartrecruit.dto.request;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
import com.ptit.thesis.smartrecruit.validation.company.profile.ValidCompanyProfile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidCompanyProfile // validate foundedIn, phone(sdt Viet Nam)
public class CompanyProfileRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 3, max = 100, message = "Company name must be between 3 and 100 characters")
    String name;

    @NotBlank(message = "Description is required")
    @Size(min = 6, max = 1000, message = "Description must be between 3 and 1000 characters")
    String description;
    
    @NotNull(message = "Organization type is required")
    OrganizationType organizationType;

    @NotNull(message = "Industry type is required")
    IndustryType industryType;

    @NotNull(message = "Team size is required")
    CompanyTeamSize teamSize;
    
    String website;
    Integer foundedIn;
    String companyVision;

    List<SocialLinkDTO> socialLinks;

    @Valid
    @NotNull(message = "Location is required")
    LocationDTO location;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Phone is required")
    String phone;
}
