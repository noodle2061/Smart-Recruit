package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDate;

import com.ptit.thesis.smartrecruit.enums.JobStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MyJobPageResponse {
    Long id;
    String slug;
    String title;
    Long daysRemaining;
    JobStatus status;
    Long numberOfapplications;
}
