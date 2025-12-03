package com.ptit.thesis.smartrecruit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateCommentRequest {
    @Schema(example = "Nội dung comment")
    @NotBlank(message = "Content is required")
    String content;
}
