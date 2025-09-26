package com.ptit.thesis.smartrecruit.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String firebaseUid;
    String fullName;
    String userName;
    String email;
    String role;
    String firebaseCustomToken;
}
