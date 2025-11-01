package com.ptit.thesis.smartrecruit.controller.candidate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
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

        CompanyProfileResponse company = companyService.getCompanyDetails(companyId);

        ApiResponse<CompanyProfileResponse> response = ApiResponse.<CompanyProfileResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get company profile successfully")
                .data(company)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Page<CompanyPageResponse>>> searchCompanies(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) OrganizationType organizationType,
            @RequestParam(required = false) IndustryType industryType,
            @RequestParam(required = false) CompanyTeamSize teamSize,
            @RequestParam(required = false) Integer foundedIn
    ) {
        Page<CompanyPageResponse> companies = companyService.searchCompanies(pageable, keyword, location, organizationType, industryType, teamSize, foundedIn);
        
        ApiResponse<Page<CompanyPageResponse>> response = ApiResponse.<Page<CompanyPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get companies successfully")
                .data(companies)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
