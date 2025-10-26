package com.ptit.thesis.smartrecruit.dto.request;

import com.ptit.thesis.smartrecruit.dto.common.LocationDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateContactInfoRequest {

    LocationDTO location;

    @Pattern(regexp = "^(\\+84|0)(3[2-9]|5[25689]|7[0|6-9]|8[1-9]|9[0-9])\\d{7}$", message = "Invalid phone number") // sdt Viet Nam
    String phone;

    @Email(message = "Invalid email address")
    String email;
}
