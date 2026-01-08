package com.ptit.thesis.smartrecruit.dto.response;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.enums.IndustryType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPageResponse {
    Long id;
    String name;
    String logoUrl;
    IndustryType industryType;
    LocationDTO location;
    Long numberOfJobs;
}
