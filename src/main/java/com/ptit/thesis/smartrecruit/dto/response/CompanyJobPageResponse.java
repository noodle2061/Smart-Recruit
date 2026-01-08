package com.ptit.thesis.smartrecruit.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
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
public class CompanyJobPageResponse {
    Long id;
    String slug;
    String jobTitle;
    JobType jobType;
    BigDecimal minSalary;
    BigDecimal maxSalary;
    SalaryType salaryType;

    // Các trường này sẽ chỉ hiển thị cho candidate đã đăng nhập
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isFavorite;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isApplied;
}
