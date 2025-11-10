package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;

public interface BlogService {
    public BlogResponse create(BlogRequest blogRequest);

    public BlogResponse update(Long id, UpdateBlogRequest updateBlogRequest);

    public BlogResponse getOne(Long id);

    public BlogResponse getOneBySlug(String slug);

    public Page<BlogResponse> listWithPage(
            String keyword,
            String sort,
            Integer page,
            Integer limit,
            List<Long> categoryIds,
            Long tagId);

    public void delete(Long id);

    public List<BlogCategoryDTO> getCategories();
}
