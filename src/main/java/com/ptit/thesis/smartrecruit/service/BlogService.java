package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;

public interface BlogService {
    public BlogResponse create(BlogRequest blogRequest);

    public BlogResponse update(Long id, UpdateBlogRequest updateBlogRequest);

    public BlogResponse getOne(Long id);

    public List<BlogResponse> listWithPage(String keyword, String sort, Integer page, Integer size);

    public void delete(Long id);
}
