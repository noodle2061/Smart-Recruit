package com.ptit.thesis.smartrecruit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyJobRequest {

    @NotNull(message = "Job id is required")
    Long jobId;

    @NotNull(message = "Resume id is required")
    Long resumeId;

    @Size(min = 50, message = "Cover letter must be at least 50 characters")
    String coverLetter;
}
