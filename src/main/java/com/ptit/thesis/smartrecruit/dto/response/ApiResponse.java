package com.ptit.thesis.smartrecruit.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    int status;
    String message;
    T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Dùng cho phân trang, lưu nội dung phân trang, các api khác không cần sử dụng", example = "null")
    Object meta;

    // public static <T> ApiResponse<T> of(int status, String message, T data) {
    //     return new ApiResponse<>(status, message, data, null);
    // }

    // public static <T> ApiResponse<T> of(int status, String message, T data, Object meta) {
    //     return new ApiResponse<>(status, message, data, meta);
    // }
}
