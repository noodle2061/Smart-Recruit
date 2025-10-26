package com.ptit.thesis.smartrecruit.dto.request;

import java.time.LocalDate;

import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.enums.Nationality;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateProfileDetailRequest {
    Nationality nationality;
    LocalDate dateOfBirth;
    Gender gender;
    String biography;
}
