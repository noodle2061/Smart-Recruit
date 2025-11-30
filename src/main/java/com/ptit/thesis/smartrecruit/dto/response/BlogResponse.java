package com.ptit.thesis.smartrecruit.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.enums.BlogStatus;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogResponse {
    Long id;
    String title;
    String slug;
    String content;
    String description;
    BlogStatus status;
    LocalDate publishedAt;
    LocalDate createdAt;
    String thumbnail;
    List<TagDTO> tags;
    Set<BlogCategoryDTO> categories;
    UserResponse author;
}
