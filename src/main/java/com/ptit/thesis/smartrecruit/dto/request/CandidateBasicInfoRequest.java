package com.ptit.thesis.smartrecruit.dto.request;

import org.hibernate.validator.constraints.URL;

import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateBasicInfoRequest {

    @Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    String fullName;

    String headline;

    ExperienceLevel experienceLevel;

    EducationLevel educationLevel;

    @Size(max = 512, message = "Personal website must be less than 255 characters")
    @URL(message = "Personal website must be a valid URL")
    String personalWebsite;
}
