package com.ptit.thesis.smartrecruit.dto.response;


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
    Object meta;

    // public static <T> ApiResponse<T> of(int status, String message, T data) {
    //     return new ApiResponse<>(status, message, data, null);
    // }

    // public static <T> ApiResponse<T> of(int status, String message, T data, Object meta) {
    //     return new ApiResponse<>(status, message, data, meta);
    // }
}
