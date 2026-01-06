package com.ptit.thesis.smartrecruit.controller.employer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.database.annotations.NotNull;
import com.ptit.thesis.smartrecruit.dto.request.CompanyProfileRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyProfileResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanySetupMetadata;
import com.ptit.thesis.smartrecruit.dto.response.CompanyStatResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/employer/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name="Employer Profile Controller", description="Quản lý công ty")
public class CompanyProfileController {
    
    CompanyService companyService;
    
    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin chi tiết của company")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> getCompanyInfo(@AuthenticationPrincipal User user) {
        CompanyProfileResponse company = companyService.getCompanyDetails(user);

        ApiResponse<CompanyProfileResponse> response = ApiResponse.<CompanyProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Get company profile successfully")
            .data(company)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    

    /**
     * Dùng để thêm mới hoặc sửa đổi thông tin của company profile
     * @param user
     * @param request
     * @param logo
     * @param banner
     * @return Thông tin Company Profile
     */
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping(value = "/setup-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thêm mới hoặc sửa đổi thông tin của company profile")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> handleCompanyProfile(@AuthenticationPrincipal User user,
                                                            @Valid @RequestPart("data") @Schema(implementation = CompanyProfileRequest.class) CompanyProfileRequest request,
                                                            @NotNull @RequestPart("logo") MultipartFile logo,
                                                            @NotNull @RequestPart("banner") MultipartFile banner) {
        CompanyProfileResponse company = companyService.createOrUpdateCompanyProfile(user, request, logo, banner);
        ApiResponse<CompanyProfileResponse> response = ApiResponse.<CompanyProfileResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Company setup successfully")
            .data(company)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/metadata")
    @Operation(summary = "Lấy metadata tạo company")
    public ResponseEntity<ApiResponse<CompanySetupMetadata>> getCompanySetupMetadata() {
        
        CompanySetupMetadata metadata = companyService.getCompanySetupMetadata();

        ApiResponse<CompanySetupMetadata> response = ApiResponse.<CompanySetupMetadata>builder()
            .status(HttpStatus.OK.value())
            .message("Get company setup metadata successfully")
            .data(metadata)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/stat")
    @Operation(summary = "Lấy thống tin thống kê của công ty")
    public ResponseEntity<ApiResponse<CompanyStatResponse>> getCompanyStat(@AuthenticationPrincipal User user) {
        CompanyStatResponse stat = companyService.getCompanyStat(user);
        ApiResponse<CompanyStatResponse> response = ApiResponse.<CompanyStatResponse>builder()
            .status(HttpStatus.OK.value())
            .message("Get company stat successfully")
            .data(stat)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
