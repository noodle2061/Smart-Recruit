package com.ptit.thesis.smartrecruit.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyStatResponse {
    Long numberOfOpenJobs;
    Long numberOfFollowedCandidates;
}
