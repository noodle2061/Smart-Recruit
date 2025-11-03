package com.ptit.thesis.smartrecruit.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
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
    String name;
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

    LocationDTO location;
    String email;
    String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isFavorite;
}
