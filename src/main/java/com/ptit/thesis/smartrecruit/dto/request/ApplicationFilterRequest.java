package com.ptit.thesis.smartrecruit.dto.request;

import com.ptit.thesis.smartrecruit.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "DTO để lọc danh sách ứng viên cho một job")
public class ApplicationFilterRequest {
    
    @Schema(description = "Lọc theo mức độ phù hợp (từ 0 đến 100)", example = "0")
    Double appropriate; // Mức độ phù hợp

    @Schema(description = "Lọc theo giới tính", example = "MALE")
    Gender gender;

    @Schema(description = "Lọc theo khoảng tuổi", example = "0-100")
    String ageRange; // Ví dụ: "18-25"

    @Schema(description = "Lọc theo ngôn ngữ (chưa hỗ trợ trong DB)")
    String language; // Tạm thời để đây, vì DB (CandidateProfile) chưa có trường này
}
