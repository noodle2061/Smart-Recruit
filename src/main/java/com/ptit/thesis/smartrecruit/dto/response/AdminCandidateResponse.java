package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCandidateResponse {
    Long id;
    Boolean isActive;
    String fullName;
    String email;
    String avatarUrl;
    LocalDateTime createdAt;
}
