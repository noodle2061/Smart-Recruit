package com.ptit.thesis.smartrecruit.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String firebaseUid;
    String fullName; // candidate
    String userName;
    String email;
    String role;
    String firebaseCustomToken;
    boolean isCompanySetup; // copmpany
    String avatar;
}
