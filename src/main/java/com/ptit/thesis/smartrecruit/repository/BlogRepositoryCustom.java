package com.ptit.thesis.smartrecruit.repository;

import com.ptit.thesis.smartrecruit.dto.common.BlogFilterDTO;
import com.ptit.thesis.smartrecruit.dto.response.AdminBlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogRepositoryCustom {
    Page<AdminBlogResponse> getBlogsForAdmin(BlogFilterDTO filter, Pageable pageable);
}
