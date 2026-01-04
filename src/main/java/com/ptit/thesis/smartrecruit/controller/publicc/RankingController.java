package com.ptit.thesis.smartrecruit.controller.publicc;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.service.RankingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api/ranking")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RankingController {

    RankingService rankingService;
    
    @GetMapping("/hot-jobs")
    public ResponseEntity<ApiResponse<List<JobPageResponse>>> getHotJobs(@RequestParam Integer limit) {
        List<JobPageResponse> jobs = rankingService.getHotJobs(limit);
        ApiResponse<List<JobPageResponse>> response = ApiResponse.<List<JobPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get hot jobs successfully")
                .data(jobs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/hot-companies")
    public ResponseEntity<ApiResponse<List<CompanyPageResponse>>> getHotCompanies(@RequestParam Integer limit) {
        List<CompanyPageResponse> companies = rankingService.getHotCompanies(limit);
        ApiResponse<List<CompanyPageResponse>> response = ApiResponse.<List<CompanyPageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get hot companies successfully")
                .data(companies)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
