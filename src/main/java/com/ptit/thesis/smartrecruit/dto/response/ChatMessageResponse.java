package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDateTime;

import com.ptit.thesis.smartrecruit.enums.MessageDirection;

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
public class ChatMessageResponse {
    Long id;
    String content;
    LocalDateTime timestampt;
    MessageDirection direction;
    boolean isRead; 
}
