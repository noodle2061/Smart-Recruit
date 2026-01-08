package com.ptit.thesis.smartrecruit.dto.common;

import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CompanyBasicInfoDTO {
    String name;
    String logoUrl;
    Integer foundedIn;
    String website;
    String email;
    String phone;
    CompanyTeamSize companySize;
}
