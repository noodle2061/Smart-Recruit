package com.ptit.thesis.smartrecruit.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.enums.SalaryType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostJobRequest {
    @NotBlank(message = "Job title is required")
    @Size(min = 5, max = 255, message = "Job title must be between 5 and 255 characters")
    String title;

    @NotBlank(message = "Job description is required")
    @Size(min = 50, message = "Job description must be at least 50 characters")
    String description;

    @NotBlank(message = "Job responsibilities are required")
    @Size(min = 50, message = "Job responsibilities must be at least 50 characters")
    String responsibilities;

    @NotNull(message = "Minimum salary is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum salary must be non-negative")
    BigDecimal minSalary;

    @NotNull(message = "Maximum salary is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Maximum salary must be non-negative")
    BigDecimal maxSalary;

    @NotNull(message = "Salary type is required")
    SalaryType salaryType;

    @NotNull(message = "Education level is required")
    EducationLevel educationLevel;

    @NotNull(message = "Experience level is required")
    ExperienceLevel experienceLevel;

    @NotNull(message = "Job type is required")
    JobType jobType;

    @NotNull(message = "Quantity (vacancies) is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer vacancies;

    @NotNull(message = "Expiration date is required")
    @FutureOrPresent(message = "Expiration date must be in the present or future")
    LocalDate expirationDate;

    List<Long> categoryIds;
}
