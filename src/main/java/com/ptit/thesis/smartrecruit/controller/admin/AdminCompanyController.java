package com.ptit.thesis.smartrecruit.controller.admin;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.AdminCompanyResponse;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Admin Company", description = "Admin Company APIs")
public class AdminCompanyController {
    
    CompanyService companyService;

    @GetMapping("")
    @Operation(summary = "Lấy danh sách các company")
    public ResponseEntity<ApiResponse<Page<AdminCompanyResponse>>> getCompanies(
        @RequestParam(required = false) String email,
        @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable) {
        Page<AdminCompanyResponse> entity = companyService.getCompaniesForAdmin(email, pageable);
        ApiResponse<Page<AdminCompanyResponse>> response = ApiResponse.<Page<AdminCompanyResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get companies successfully")
                .data(entity)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Lấy chi tiết thông tin của một company")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> getCompanyDetail(@PathVariable Long companyId) {
        CompanyProfileResponse entity = companyService.getCompanyDetails(companyId);
        ApiResponse<CompanyProfileResponse> response = ApiResponse.<CompanyProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get company successfully")
                .data(entity)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("/{companyId}/deactivate")
    @Operation(summary = "Vô hiệu hóa một tài khoản company")
    public ResponseEntity<ApiResponse<Void>> deactivateCompany(@PathVariable Long companyId) {
        companyService.deactivateCompany(companyId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete company successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{companyId}/activate")
    @Operation(summary = "Kích hoạt lại một tài khoản company")
    public ResponseEntity<ApiResponse<Void>> activateCompany(@PathVariable Long companyId) {
        companyService.activateCompany(companyId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("activate company successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
