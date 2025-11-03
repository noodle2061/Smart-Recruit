package com.ptit.thesis.smartrecruit.dto.request;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateBlogRequest {
    @Schema(example = "update nội dung blog")
    String content;

    @Schema(example = "Update mô tả blog")
    String description;

    @Schema(example = "PUBLISHED", allowableValues = { "DRAFT", "PUBLISHED", "REQUESTED" })
    BlogStatus status;

    @Schema(example = "[\"#dev\", \"#devfe\", \"devbe\"]")
    Set<String> tags;

    @Schema(example = "[\"1\", \"2\", \"3\"]")
    List<Long> blogCategoryIds;

    // MultipartFile thumbnail;
}
