package com.ptit.thesis.smartrecruit.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogRequest {
    @Schema(example = "Tiêu đề blog example")
    @NotBlank(message = "title is required")
    @Size(min = 5, max = 255, message = "title must be between 5 and 255 characters")
    String title;

    @Schema(example = "tieu-de-blog-example")
    @NotBlank(message = "slug is required")
    @Size(min = 5, max = 255, message = "slug must be between 5 and 255 characters")
    String slug;

    @Schema(example = "Nội dung blog...")
    @NotBlank(message = "content is required")
    String content;

    @Schema(example = "Mô tả blog...")
    String description;

    @Schema(example = "PUBLISHED")
    BlogStatus status;

    @Schema(example = "2025-12-01T06:43:29.804")
    @FutureOrPresent(message = "published-at date must be in the present or future")
    LocalDateTime publishedAt;

    @Schema(example = "[\"#dev\", \"#devfe\", \"devbe\"]")
    List<String> tags;

    @Schema(example = "[\"1\", \"2\", \"3\"]")
    List<Long> blogCategoryIds;

    MultipartFile thumbnail;
}
