package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO các đơn ứng tuyển cho một job của employer, sử dụng cho phân trang.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationBriefResponse {
    Long applicationId;
    Long candidateId;
    String candidateName;
    String candidateAvatarUrl;
    String candidateHeadline;
    String candidateEmail;
    ExperienceLevel experienceLevel;
    EducationLevel educationLevel;
    LocalDateTime appliedAt;
    String resumeUrl;
    String coverLetter;
    Double score;
}
