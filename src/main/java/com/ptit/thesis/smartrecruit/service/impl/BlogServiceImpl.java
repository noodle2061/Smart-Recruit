package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.entity.Blog;
import com.ptit.thesis.smartrecruit.entity.BlogCategory;
import com.ptit.thesis.smartrecruit.entity.Tag;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.BlogStatus;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.mapper.BlogMapper;
import com.ptit.thesis.smartrecruit.repository.BlogCategoryRepository;
import com.ptit.thesis.smartrecruit.repository.BlogRepository;
import com.ptit.thesis.smartrecruit.service.BlogService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.AuthUtil;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
    BlogCategoryRepository blogCategoryRepository;
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
    public BlogResponse getOneBySlug(String slug) {
        Blog blog = this.blogRepository.findBySlug(slug).orElse(null);
        if (blog == null) {
            throw new IllegalStateException("blog with slug " + slug + " not found");
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
        User currentUser = AuthUtil.getCurrentUser();

        boolean isAdmin = "ADMIN".equals(currentUser.getRole().getName());

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        boolean isMyBlog = blog.getAuthor().getId().equals(currentUser.getId());

        if (!isAdmin && !isMyBlog) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not permission delete this blog");
        }

        blogRepository.delete(blog);
    }

    @Override
    public Page<BlogResponse> listWithPage(
            String keyword,
            String sort,
            Integer page,
            Integer limit,
            List<Long> categoryIds,
            Long tagId) {
        Specification<Blog> spec = Specification.unrestricted();

        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.and(
                    cb.equal(root.get("status"), BlogStatus.PUBLISHED),
                    cb.or(
                            cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%"))));
        }

        if (categoryIds != null && categoryIds.size() > 0) {
            spec = spec.and((root, query, cb) -> {
                Join<Blog, BlogCategory> categoryJoin = root.join("categories", JoinType.LEFT);

                return categoryJoin.get("id").in(categoryIds);
            });
        }

        if (tagId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Blog, Tag> tagJoin = root.join("tags", JoinType.LEFT);

                return cb.equal(tagJoin.get("id"), tagId);
            });
        }

        Sort _sort = Sort.by(
                sort != null && !sort.isBlank() && sort.charAt(0) == '-' ? Sort.Direction.DESC : Sort.Direction.ASC,
                sort != null ? sort.substring(1) : "id");

        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), BlogStatus.PUBLISHED));

        Pageable pageable = PageRequest.of(page, limit, _sort);
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        Page<BlogResponse> pagination = blogs.map(blog -> blogMapper.toBlogResponse(blog));
        return pagination;
    }

    @Override
    public List<BlogCategoryDTO> getCategories() {
        List<BlogCategory> categories = this.blogCategoryRepository.findAll();
        return categories
                .stream()
                .map(cate -> new BlogCategoryDTO(cate.getId(), cate.getName()))
                .toList();
    }

    @Override
    public List<TagDTO> getPopularTags() {
        return this.blogCategoryRepository.findPopularTags();
    }

    @Override
    public Page<BlogResponse> listWithPageOfUser(Long id, String keyword, String sort, Integer page, Integer limit,
            List<Long> categoryIds, Long tagId) {
        Specification<Blog> spec = Specification.unrestricted();

        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%")));
        }

        if (categoryIds != null && categoryIds.size() > 0) {
            spec = spec.and((root, query, cb) -> {
                Join<Blog, BlogCategory> categoryJoin = root.join("categories", JoinType.LEFT);

                return categoryJoin.get("id").in(categoryIds);
            });
        }

        if (tagId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Blog, Tag> tagJoin = root.join("tags", JoinType.LEFT);

                return cb.equal(tagJoin.get("id"), tagId);
            });
        }

        Sort _sort = Sort.by(
                sort != null && !sort.isBlank() && sort.charAt(0) == '-' ? Sort.Direction.DESC : Sort.Direction.ASC,
                sort != null ? sort.substring(1) : "id");

        spec = spec.and((root, query, cb) -> cb.equal(root.get("author").get("id"), id));

        Pageable pageable = PageRequest.of(page - 1, limit, _sort);
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        Page<BlogResponse> pagination = blogs.map(blog -> blogMapper.toBlogResponse(blog));
        return pagination;
    }

}
