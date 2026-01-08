package com.ptit.thesis.smartrecruit.dto.common;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogFilterDTO {
    @Schema(description = "Tìm kiếm theo keyword")
    String keyword;

    @Schema(description = "Lọc theo trạng thái bài đăng")
    BlogStatus status;
}
