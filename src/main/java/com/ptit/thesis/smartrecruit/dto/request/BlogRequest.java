package com.ptit.thesis.smartrecruit.dto.request;

import java.time.LocalDateTime;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogRequest {
    @NotBlank(message = "title is required")
    @Size(min = 5, max = 255, message = "title must be between 5 and 255 characters")
    String title;

    @NotBlank(message = "slug is required")
    @Size(min = 5, max = 255, message = "title must be between 5 and 255 characters")
    String slug;

    @NotBlank(message = "content is required")
    String content;

    String description;

    BlogStatus status;

    @FutureOrPresent(message = "published-at date must be in the present or future")
    LocalDateTime publishedAt;
}
