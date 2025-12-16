package com.ptit.thesis.smartrecruit.service;

import java.util.List;

import com.ptit.thesis.smartrecruit.dto.response.AdminBlogResponse;
import org.springframework.data.domain.Page;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    BlogResponse create(BlogRequest blogRequest);

    BlogResponse update(Long id, UpdateBlogRequest updateBlogRequest);

    BlogResponse getOne(Long id);

    BlogResponse getOneBySlug(String slug);

    BlogResponse getMyBlogBySlug(String slug);

    Page<BlogResponse> listWithPage(
            String keyword,
            String sort,
            Integer page,
            Integer limit,
            List<Long> categoryIds,
            Long tagId);

    Page<BlogResponse> listWithPageOfUser(
            Long id,
            String keyword,
            String sort,
            Integer page,
            Integer limit,
            List<Long> categoryIds,
            Long tagId);

    void delete(Long id);

    List<BlogCategoryDTO> getCategories();

    List<TagDTO> getPopularTags();

    Page<AdminBlogResponse> getBlogsForAdmin(Pageable pageable);

}
