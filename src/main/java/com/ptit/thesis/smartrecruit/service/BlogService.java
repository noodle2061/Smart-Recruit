package com.ptit.thesis.smartrecruit.service;

import org.springframework.data.domain.Page;

import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;

public interface BlogService {
    public BlogResponse create(BlogRequest blogRequest);

    public BlogResponse update(Long id, UpdateBlogRequest updateBlogRequest);

    public BlogResponse getOne(Long id);

    public BlogResponse getOneBySlug(String slug);

    public Page<BlogResponse> listWithPage(String keyword, String sort, Integer page, Integer limit);

    public void delete(Long id);
}
