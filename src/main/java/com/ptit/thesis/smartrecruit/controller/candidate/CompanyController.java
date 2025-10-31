package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.service.CompanyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Company Controller", description = "Lấy thông tin công ty cho candidate và khách vãng lai")
public class CompanyController {

    CompanyService companyService;

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> getCompanyInfo(@PathVariable Long companyId) {

        CompanyProfileResponse company = companyService.getCompanyInfo(companyId);

        ApiResponse<CompanyProfileResponse> response = ApiResponse.<CompanyProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get company profile successfully")
                .data(company)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
