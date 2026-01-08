package com.ptit.thesis.smartrecruit.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.dto.common.JobCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.enums.SalaryType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDetailResponse {
    Long id;
    CompanyBasicInfoDTO company;
    String title;
    String description;
    String responsibilities;
    BigDecimal minSalary;
    BigDecimal maxSalary;
    SalaryType salaryType;
    LocationDTO location; 
    EducationLevel educationLevel; 
    ExperienceLevel experienceLevel; 
    JobType jobType;
    Integer vacancies;
    LocalDate expirationDate;
    JobStatus status;
    String slug;
    Boolean isFeatured;
    LocalDateTime postedAt;
    List<JobCategoryDTO> jobCategories;

    // hai trường chỉ hiển thị cho candidate, các vai trò khác sẽ set là null và không hiển thị
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isFavorite;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean isApplied;
}
