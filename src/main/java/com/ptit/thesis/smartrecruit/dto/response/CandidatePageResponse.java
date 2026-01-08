package com.ptit.thesis.smartrecruit.dto.response;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePageResponse {
    Long id;
    String fullName;
    String avatarUrl;
    String headline;
    String email;
    ExperienceLevel experienceLevel;
    EducationLevel educationLevel;
    LocationDTO location;
    boolean isFollow;
}
