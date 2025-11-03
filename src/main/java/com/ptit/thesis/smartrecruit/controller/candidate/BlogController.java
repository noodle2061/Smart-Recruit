package com.ptit.thesis.smartrecruit.controller.candidate;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.service.BlogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @GetMapping("")
    @Operation(summary = "Lấy danh sách bài viết")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<List<BlogResponse>>> listWithPage(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(required = false, defaultValue="-id") String sort
    ) {
        List<BlogResponse> blogs = this.blogService.listWithPage(keyword, sort, page, size);

        ApiResponse<List<BlogResponse>> response = ApiResponse.<List<BlogResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all blog successful")
                .data(blogs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết bài viết")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<BlogResponse>> getOne(@PathVariable Long id) {
        BlogResponse blog = this.blogService.getOne(id);

        ApiResponse<BlogResponse> response = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get detail blog successful")
                .data(blog)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Tạo một bài viết mới")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogResponse>> create(@Valid @ModelAttribute BlogRequest request) {
        BlogResponse newBlog = this.blogService.create(request);

        ApiResponse<BlogResponse> response = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Create blog successful")
                .data(newBlog)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Cập nhật một bài viết")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BlogResponse>> update(
            @Valid @ModelAttribute UpdateBlogRequest request,
            @PathVariable Long id) {
        BlogResponse blog = this.blogService.update(id, request);

        ApiResponse<BlogResponse> response = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Create blog successful")
                .data(blog)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một bài viết")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        this.blogService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Delete blog successful")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
