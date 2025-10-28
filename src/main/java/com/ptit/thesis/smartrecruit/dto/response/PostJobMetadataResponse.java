package com.ptit.thesis.smartrecruit.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostJobMetadataResponse {
    Map<String, String> salaryTypes;
    Map<String, String> educationLevels;
    Map<String, String> experienceLevels;
    Map<String, String> jobTypes;
    Map<Long, String> jobCategories;
}
