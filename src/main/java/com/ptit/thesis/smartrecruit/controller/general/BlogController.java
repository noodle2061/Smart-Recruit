package com.ptit.thesis.smartrecruit.controller.general;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.service.BlogService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("api/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "BlogController", description = "Quản lý bài viết")
public class BlogController {
    BlogService blogService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> create() {
        ApiResponse<String> s = ApiResponse.<String>builder().status(HttpStatus.CREATED.value())
            .message("Create blog successful")
            .data("HAHAHAH")
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }
}
