package com.ptit.thesis.smartrecruit.controller.publicc;

import java.util.List;

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
import com.ptit.thesis.smartrecruit.dto.response.CompanyJobPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.dto.response.PageResponse;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
import com.ptit.thesis.smartrecruit.service.CompanyService;
import com.ptit.thesis.smartrecruit.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Public Company Controller", description = "Thông tin public công ty cho user")
public class PublicCompanyController {

    CompanyService companyService;
    JobService jobService;

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Lấy thông tin chi tiết của công ty cho Candidate và khách vãng lai.")
    @SecurityRequirements()
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
    @Operation(summary = "Lấy dữ liệu phân trang có filter các công ty cho Candidate và khách vãng lai. Giao diện chính sử dụng.")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<List<CompanyPageResponse>>> searchCompanies(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) OrganizationType organizationType,
            @RequestParam(required = false) IndustryType industryType,
            @RequestParam(required = false) CompanyTeamSize teamSize,
            @RequestParam(required = false) Integer foundedIn) {
        Page<CompanyPageResponse> companies = companyService.searchCompanies(pageable, keyword, location,
                organizationType, industryType, teamSize, foundedIn);

        ApiResponse<List<CompanyPageResponse>> response = ApiResponse.<List<CompanyPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get companies successfully")
                .data(companies.getContent())
                .meta(PageResponse.of(companies))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/company/{companyId}/jobs")
    @Operation(summary = "Lấy dữ liệu phân trang các job của một công ty cho Candidate và khách vãng lai.")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<Page<CompanyJobPageResponse>>> getJobsForCompany(
            @PathVariable Long companyId,
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable) {

        Page<CompanyJobPageResponse> jobs = jobService.getJobsByCompany(companyId, pageable);

        ApiResponse<Page<CompanyJobPageResponse>> response = ApiResponse.<Page<CompanyJobPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get company jobs successfully")
                .data(jobs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
