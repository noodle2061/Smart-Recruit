package com.ptit.thesis.smartrecruit.dto.response;

import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.BlogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBlogResponse {
    Long id;
    String title;
    BlogStatus status;
    LocalDateTime createdAt;
    String thumbnail;
    UserResponse author;
}
