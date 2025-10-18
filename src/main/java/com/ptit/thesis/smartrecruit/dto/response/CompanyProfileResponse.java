package com.ptit.thesis.smartrecruit.dto.response;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyProfileResponse {
    String companyName;
    String description;
    String logoUrl;
    String bannerUrl;
    
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
