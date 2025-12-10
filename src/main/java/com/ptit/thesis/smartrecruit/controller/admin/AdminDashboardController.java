package com.ptit.thesis.smartrecruit.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.AdminDashboardStatResponse;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.service.AdminDashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/admin/dashboard")
@Tag(name = "Admin Dashboard", description = "Admin Dashboard Stats APIs")
public class AdminDashboardController {

    AdminDashboardService adminDashboardService;
    
    @GetMapping("/stats")
    @Operation(summary = "Lấy thống kê dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardStatResponse>> getDashboardStats() {
        AdminDashboardStatResponse dashboardStats = adminDashboardService.getDashboardStat();

        ApiResponse<AdminDashboardStatResponse> response = ApiResponse.<AdminDashboardStatResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get dashboard stats successfully")
                .data(dashboardStats)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
