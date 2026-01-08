package com.ptit.thesis.smartrecruit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatResponse {
    Long totalCandidates;
    Long totalCompanies;
    Long totalJobs;
    Long totalBlogs;
}
