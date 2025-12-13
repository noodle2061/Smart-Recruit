package com.ptit.thesis.smartrecruit.dto.request;

import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationStatusUpdateRequest {
    JobApplicationStatus status;
}
