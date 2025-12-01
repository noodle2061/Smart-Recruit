package com.ptit.thesis.smartrecruit.dto.request;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateBlogRequest {
    @Schema(example = "Tiêu đề blog example", nullable = true)
    @Size(min = 5, max = 255, message = "title must be between 5 and 255 characters")
    String title;

    @Schema(example = "tieu-de-blog-example", nullable = true)
    @Size(min = 5, max = 255, message = "slug must be between 5 and 255 characters")
    String slug;

    @Schema(example = "update nội dung blog", nullable = true)
    String content;

    @Schema(example = "Update mô tả blog", nullable = true)
    String description;

    @Schema(example = "PUBLISHED", nullable = true)
    BlogStatus status;

    @Schema(example = "[\"#dev\", \"#devfe\", \"devbe\"]", nullable = true)
    Set<String> tags;

    @Schema(example = "[\"1\", \"2\", \"3\"]", nullable = true)
    List<Long> blogCategoryIds;

    @Schema(type = "string", format = "binary", nullable = true)
    MultipartFile thumbnail;
}
