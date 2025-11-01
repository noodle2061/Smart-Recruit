package com.ptit.thesis.smartrecruit.dto.response;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;

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
    LocationDTO location;
    Long numberOfJobs;
}
