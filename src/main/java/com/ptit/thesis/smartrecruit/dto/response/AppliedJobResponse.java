package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppliedJobResponse {
    Long id;
    String slug;
    String jobTitle;
    String provinceCity;
    String companyName;
    String companyLogoUrl;
    String salary;// min + max + type
    JobType type;
    JobStatus jobStatus;
    LocalDateTime appliedDate;
}
