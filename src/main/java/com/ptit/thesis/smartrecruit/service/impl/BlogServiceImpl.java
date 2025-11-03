package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.entity.Blog;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.mapper.BlogMapper;
import com.ptit.thesis.smartrecruit.repository.BlogRepository;
import com.ptit.thesis.smartrecruit.service.BlogService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.AuthUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    BlogMapper blogMapper;
    BlogRepository blogRepository;
    S3Service s3Service;

    @Override
    public BlogResponse create(BlogRequest blogRequest) {
        User author = AuthUtil.getCurrentUser();
        Blog newBlog = blogMapper.toEntity(blogRequest);
        String slug = newBlog.getSlug();

        Blog existBlog = blogRepository.findOne((root, query, cb) -> cb.equal(root.get("slug"), slug)).orElse(null);

        if (existBlog != null) {
            throw new IllegalStateException("Blog not same slug");
        }
        newBlog.setAuthor(author);

        // upload thumbnail
        try {
            MultipartFile thumbnailFile = blogRequest.getThumbnail();
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                String newThumbnailKey = this.s3Service.uploadFile(thumbnailFile, "blogs/thumbnails");
                newBlog.setThumbnail(newThumbnailKey);
            }
        } catch (IOException e) {
            log.error("Error uploading file to S3: {}", e.getMessage());
            throw new S3ErrorException("Error uploading file to S3: " + e.getMessage());
        }

        Blog saved = this.blogRepository.save(newBlog);

        return blogMapper.toBlogResponse(saved);
    }

    @Override
    public BlogResponse getOne(Long id) {
        Blog blog = this.blogRepository.findById(id).orElse(null);
        if (blog == null) {
            throw new IllegalStateException("blog with id " + id + " not found");
        }

        return blogMapper.toBlogResponse(blog);
    }

    @Override
    public BlogResponse update(Long id, UpdateBlogRequest updateBlogRequest) {
        Blog blog = this.blogRepository.findById(id).orElse(null);
        if (blog == null)
            throw new IllegalStateException("not found blog with id " + id + " to update");

        blogMapper.toUpdateEntity(updateBlogRequest, blog);

        // upload thumbnail
        // try {
        // MultipartFile thumbnailFile = updateBlogRequest.getThumbnail();
        // if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
        // String oldKey = blog.getThumbnail();
        // if(oldKey != null && !oldKey.isEmpty()) {
        // this.s3Service.deleteFileByKey(oldKey);
        // }
        // String newThumbnailKey = this.s3Service.uploadFile(thumbnailFile,
        // "blogs/thumbnails");
        // blog.setThumbnail(newThumbnailKey);
        // }
        // } catch (IOException e) {
        // log.error("Error uploading file to S3: {}", e.getMessage());
        // throw new S3ErrorException("Error uploading file to S3: " + e.getMessage());
        // }

        Blog updatedBlog = this.blogRepository.save(blog);

        return blogMapper.toBlogResponse(updatedBlog);
    }

    @Override
    public void delete(Long id) {
        Blog blog = this.blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "blog not found"));
        this.blogRepository.delete(blog);
    }

    @Override
    public List<BlogResponse> listWithPage(String keyword, String sort, Integer page, Integer size) {
        Specification<Blog> spec = Specification.unrestricted();
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(root.get("content"), "%" + keyword.toLowerCase() + "%")));
        }

        Sort _sort = Sort.by(
                sort != null && !sort.isBlank() && sort.charAt(0) == '-' ? Sort.Direction.DESC : Sort.Direction.ASC,
                sort != null ? sort.substring(1) : "id");

        Pageable pageable = PageRequest.of(page, size, _sort);
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);

        return blogs.map(blogMapper::toBlogResponse).toList();
    }

}
