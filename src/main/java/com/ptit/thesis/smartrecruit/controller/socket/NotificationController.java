package com.ptit.thesis.smartrecruit.controller.socket;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.NotificationResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("/api/notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Quản lý thông báo")
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("")
    @Operation(summary = "Lấy danh sách thông báo của tài khoản")
    public ResponseEntity<ApiResponse<Slice<NotificationResponse>>> getMyNotifications(
        @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
        @AuthenticationPrincipal User user) {
        Slice<NotificationResponse> notifications = notificationService.getNotifications(user, pageable);
        ApiResponse<Slice<NotificationResponse>> response = ApiResponse.<Slice<NotificationResponse>>builder()
            .status(HttpStatus.OK.value())
            .message("Get notifications successfully")
            .data(notifications)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/unread-count")
    @Operation(summary = "Lấy số lượng thông báo chưa đọc")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@AuthenticationPrincipal User user) {
        Long count = notificationService.countUnread(user);
        ApiResponse<Long> response = ApiResponse.<Long>builder()
            .status(HttpStatus.OK.value())
            .message("Get unread count successfully")
            .data(count)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Đánh dấu một thông báo là đã đọc, sử dụng khi user click vào riêng một thông báo.")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@AuthenticationPrincipal User user, Long notificationId) {
        notificationService.markAsRead(notificationId, user);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message("Mark read successfully")
            .data(null)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PostMapping("/read-all")
    @Operation(summary = "Đánh dấu tất tất cả thông báo được đọc, khi user nhấn nút đã xem toàn bộ thông báo.")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@AuthenticationPrincipal User user) {
        notificationService.markReadAll(user);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .status(HttpStatus.OK.value())
            .message("Mark all read successfully")
            .data(null)
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
