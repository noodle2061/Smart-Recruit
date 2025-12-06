package com.ptit.thesis.smartrecruit.controller.candidate;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import com.ptit.thesis.smartrecruit.dto.response.PageResponse;
import com.ptit.thesis.smartrecruit.service.BlogService;
import com.ptit.thesis.smartrecruit.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/blogs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "BlogController", description = "Quản lý bài viết")
public class BlogController {
    BlogService blogService;
    CommentService commentService;

    @GetMapping("")
    @Operation(summary = "Lấy danh sách bài viết")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<List<BlogResponse>>> listWithPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "+createdAt") String sort,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Long tagId) {
        Page<BlogResponse> blogs = this.blogService.listWithPage(keyword, sort, page, limit,
                categoryIds,
                tagId);

        ApiResponse<List<BlogResponse>> response = ApiResponse.<List<BlogResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all blog successful")
                .data(blogs.getContent())
                .meta(PageResponse.of(blogs))
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

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Lấy chi tiết bài viết qua slug")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<BlogResponse>> getOneBySlug(@PathVariable String slug) {
        BlogResponse blog = this.blogService.getOneBySlug(slug);

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

    @GetMapping("/categories")
    @Operation(summary = "Lấy danh sách blog category")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<List<BlogCategoryDTO>>> getCategories() {

        List<BlogCategoryDTO> data = this.blogService.getCategories();

        ApiResponse<List<BlogCategoryDTO>> response = ApiResponse.<List<BlogCategoryDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("get categories of blog successfully.")
                .data(data)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/popular-tags")
    @Operation(summary = "Lấy danh sách tag phổ biến của blog")
    @SecurityRequirements()
    public ResponseEntity<ApiResponse<List<TagDTO>>> getPopularTags() {

        List<TagDTO> data = this.blogService.getPopularTags();

        ApiResponse<List<TagDTO>> response = ApiResponse.<List<TagDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("get tags of blog successfully.")
                .data(data)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Lấy danh sách bài viết của 1 user")
    @SecurityRequirements
    public ResponseEntity<ApiResponse<List<BlogResponse>>> getBlogOfUser(
            @PathVariable Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "+createdAt") String sort,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) Long tagId) {
        Page<BlogResponse> blogs = this.blogService.listWithPageOfUser(id, keyword, sort, page,
                limit,
                categoryIds,
                tagId);

        ApiResponse<List<BlogResponse>> response = ApiResponse.<List<BlogResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get all blog of user_id " + id)
                .data(blogs.getContent())
                .meta(PageResponse.of(blogs))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my/{slug}")
    @Operation(summary = "Lấy chi tiết bài viết của tôi")
    public ResponseEntity<ApiResponse<BlogResponse>> getMyBlog(
            @PathVariable String slug) {
        BlogResponse blog = this.blogService.getMyBlogBySlug(slug);

        ApiResponse<BlogResponse> response = ApiResponse.<BlogResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get detail blog successful")
                .data(blog)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Tạo comment cho blog")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = this.commentService.createCommentBlog(id, commentRequest);

        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("create comment blog successful")
                .data(comment)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "Lấy các comment của một bài viết")
    @SecurityRequirements
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsOfBlog(
            @PathVariable Long id
    ) {
        List<CommentResponse> comments = this.commentService.getCommentsOfBlog(id);

        ApiResponse<List<CommentResponse>> response = ApiResponse.<List<CommentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy comments của bài viết thành công")
                .data(comments)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
