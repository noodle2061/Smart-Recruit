package com.ptit.thesis.smartrecruit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long id;
    Long parentId;
    String content;
    LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserResponse createdBy;
}
