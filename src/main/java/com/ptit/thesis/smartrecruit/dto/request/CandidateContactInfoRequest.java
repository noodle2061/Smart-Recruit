package com.ptit.thesis.smartrecruit.dto.request;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;
import com.ptit.thesis.smartrecruit.utils.Constraint;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateContactInfoRequest {

    LocationDTO location;

    @Pattern(regexp = Constraint.INTERNATIONAL_PHONE_REGEX, message = "Invalid phone number")
    String phone;
}
