package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AdminCompanyResponse {
    Long id;
    Boolean isActive;
    String name;
    String email;
    String logoUrl;
    LocalDateTime createdAt;
}
