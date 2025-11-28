package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import com.ptit.thesis.smartrecruit.enums.NotificationType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long id;
    String content;
    boolean isRead;
    NotificationType type;
    Long relatedId;
    LocalDateTime createdAt;

    Long senderId;
    String senderName;
    String senderAvatarUrl;
}
