package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.dto.common.SocialLinkDTO;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.enums.MaritalStatus;
import com.ptit.thesis.smartrecruit.enums.Nationality;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateProfileResponse {
    Long id;
    String fullName;
    String avatarUrl;
    String headline;
    ExperienceLevel experienceLevel;
    EducationLevel educationLevel;
    String personalWebsite;
    Nationality nationality;
    LocalDate dateOfBirth;
    Gender gender;
    MaritalStatus maritalStatus;
    String biography;
    String phone;
    String email; // lấy từ bảng user
    Boolean isPublic; // mặc định là true
    LocationDTO location;
    List<SocialLinkDTO> socialLinks;
}
