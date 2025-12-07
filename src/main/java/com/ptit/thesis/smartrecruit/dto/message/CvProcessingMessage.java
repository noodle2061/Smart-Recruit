package com.ptit.thesis.smartrecruit.dto.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CvProcessingMessage {
    Long applicationId;
    String fileUrl;
    Integer version;
    String jobTitle;
    String jobDescription;
    String jobResponsibilities;
    String educationLevel;
    String experienceLevel;
}