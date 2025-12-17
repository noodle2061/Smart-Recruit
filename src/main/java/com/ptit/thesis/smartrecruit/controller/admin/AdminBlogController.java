package com.ptit.thesis.smartrecruit.controller.admin;

import com.ptit.thesis.smartrecruit.dto.common.BlogFilterDTO;
import com.ptit.thesis.smartrecruit.dto.response.AdminBlogResponse;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.dto.response.PageResponse;
import com.ptit.thesis.smartrecruit.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "AdminBlogController", description = "Admin quản lý bài viết")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {
    BlogService blogService;

    @GetMapping("")
    @Operation(summary = "Admin lấy danh sách blog")
    public ResponseEntity<ApiResponse<List<AdminBlogResponse>>> getBlogs(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @ParameterObject BlogFilterDTO filter
    ) {
        Page<AdminBlogResponse> blogs = this.blogService.getBlogsForAdmin(filter, pageable);
        ApiResponse<List<AdminBlogResponse>> response = ApiResponse.<List<AdminBlogResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách blog thành công")
                .data(blogs.getContent())
                .meta(PageResponse.of(blogs))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("{id}/publish")
    @Operation(summary = "Admin duyệt đăng tải blog")
    public ResponseEntity<ApiResponse<Boolean>> publish(@PathVariable Long id) {
        this.blogService.publish(id);
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
            .status(HttpStatus.OK.value())
            .message("Publish bài viết thành công")
            .data(true)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Admin xóa 1 blog")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Long id) {
        this.blogService.adminDelete(id);
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
            .status(HttpStatus.OK.value())
            .message("Xóa bài viết thành công")
            .data(true)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
