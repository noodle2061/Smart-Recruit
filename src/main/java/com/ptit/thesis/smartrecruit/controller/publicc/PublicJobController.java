package com.ptit.thesis.smartrecruit.controller.publicc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.service.JobService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController
@RequestMapping("/api") // không để api /public vì còn để phân biệt người bình thường với candidate để điều chỉnh dto
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PublicJobController {
    
    JobService jobService;

    @GetMapping("/job/{slug}")
    public ResponseEntity<ApiResponse<JobDetailResponse>> getJobDetailBySlug(@PathVariable String slug) {
        JobDetailResponse jobDetailResponse = jobService.getJobDetail(slug);

        ApiResponse<JobDetailResponse> response = ApiResponse.<JobDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get job detail successfully")
                .data(jobDetailResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
