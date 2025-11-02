package com.ptit.thesis.smartrecruit.dto.response;

import java.math.BigDecimal;

import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.enums.SalaryType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class JobPageResponse {
    Long id;
    String slug;
    String jobTitle;
    String companyName;
    String companyLogoUrl;
    String provinceCity;
    JobType jobType;
    BigDecimal minSalary;
    BigDecimal maxSalary;
    SalaryType salaryType;
}
