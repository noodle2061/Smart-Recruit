package com.ptit.thesis.smartrecruit.controller.candidate;

import com.ptit.thesis.smartrecruit.dto.request.UpdateCommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import com.ptit.thesis.smartrecruit.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "CommentController", description = "Quản lý comment")
public class CommentController {
    CommentService commentService;

    @PatchMapping("/{id}")
    @Operation(summary = "Sửa comment của tôi")
    public ResponseEntity<ApiResponse<CommentResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody UpdateCommentRequest request) {
        CommentResponse comment = this.commentService.updateComment(id, request);

        ApiResponse<CommentResponse> response = ApiResponse.<CommentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật comment thành công")
                .data(comment)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa comment của tôi")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        this.commentService.deleteComment(id);

        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa comment thành công")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
