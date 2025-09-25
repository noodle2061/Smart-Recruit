package com.ptit.thesis.smartrecruit.dto.request;

import com.ptit.thesis.smartrecruit.validation.user.ValidRole;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuthRegisterRequest {

    @NotBlank(message = "Role is required")
    @ValidRole
    String role;
}
