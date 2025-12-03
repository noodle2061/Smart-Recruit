package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDate;

import com.ptit.thesis.smartrecruit.entity.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    Long commentableId;
    String commentableType;
    User user;
    String content;
    LocalDate createdAt;
}
