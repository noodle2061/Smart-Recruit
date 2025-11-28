package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    Long conversationId;

    // Thông tin bạn chat
    Long partnerId;
    String partnerName;
    String partnerAvatarUrl;

    String lastMessage;
    LocalDateTime lastMessageAt;

    Long unreadCount;
}
