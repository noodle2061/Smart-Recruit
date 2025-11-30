package com.ptit.thesis.smartrecruit.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ptit.thesis.smartrecruit.dto.common.BlogCategoryDTO;
import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.dto.request.BlogRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateBlogRequest;
import com.ptit.thesis.smartrecruit.dto.response.BlogResponse;
import com.ptit.thesis.smartrecruit.entity.Blog;
import com.ptit.thesis.smartrecruit.entity.BlogCategory;
import com.ptit.thesis.smartrecruit.entity.Tag;
import com.ptit.thesis.smartrecruit.repository.BlogCategoryRepository;
import com.ptit.thesis.smartrecruit.repository.TagRepository;
import com.ptit.thesis.smartrecruit.service.S3Service;

import jakarta.transaction.Transactional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BlogMapper {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserMapper userMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    abstract public Blog toEntity(BlogRequest blogRequest);

    @AfterMapping
    @Transactional
    protected void handleRequestToBlog(BlogRequest blogRequest, @MappingTarget Blog blog) {
        // set Tags
        if (blogRequest.getTags() != null && !blogRequest.getTags().isEmpty()) {

            List<Tag> existedTags = this.tagRepository.findAllByNameIn(blogRequest.getTags());
            Set<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toSet());
            List<Tag> newTags = blogRequest.getTags()
                    .stream()
                    .filter(tagName -> !existedTagNames.contains(tagName))
                    .map(tagName -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }).toList();

            Set<Tag> allTags = new HashSet<>();
            allTags.addAll(existedTags);
            allTags.addAll(newTags);

            blog.setTags(allTags);
        }

        // set category
        if (blogRequest.getBlogCategoryIds() != null && !blogRequest.getBlogCategoryIds().isEmpty()) {

            Set<Long> ids = new HashSet<>(blogRequest.getBlogCategoryIds());
            List<BlogCategory> categories = this.blogCategoryRepository.findAllById(ids);
            blog.setCategories(categories.stream().collect(Collectors.toSet()));
        }

    }

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    abstract public BlogResponse toBlogResponse(Blog blog);

    @AfterMapping
    protected void handleBlogToResponse(Blog blog, @MappingTarget BlogResponse blogResponse) {
        if (blog.getTags() != null && !blog.getTags().isEmpty()) {
            List<TagDTO> tags = blog.getTags()
                    .stream()
                    .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                    .toList();
            blogResponse.setTags(tags);
        }

        if (blog.getCategories() != null && !blog.getCategories().isEmpty()) {
            Set<BlogCategoryDTO> categories = blog.getCategories()
                    .stream()
                    .map(category -> new BlogCategoryDTO(category.getId(), category.getName()))
                    .collect(Collectors.toSet());

            blogResponse.setCategories(categories);
        }

        // set thumbnail
        String thumbnailUrl = this.s3Service.generatePresignedUrl(blog.getThumbnail());
        blogResponse.setThumbnail(thumbnailUrl);

        // set author
        blogResponse.setAuthor(this.userMapper.toUserResponse(blog.getAuthor()));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    abstract public void toUpdateEntity(UpdateBlogRequest updateBlogRequest, @MappingTarget Blog blog);

    @AfterMapping
    protected void handleUpdateBlogToResponse(UpdateBlogRequest updateBlogRequest, @MappingTarget Blog blog) {
        if (updateBlogRequest.getTags() != null && !updateBlogRequest.getTags().isEmpty()) {
            List<String> updatedTagsName = updateBlogRequest.getTags().stream().collect(Collectors.toList());
            List<Tag> existedTags = this.tagRepository.findAllByNameIn(updatedTagsName);
            List<String> existedTagNames = existedTags.stream().map(Tag::getName).collect(Collectors.toList());
            List<Tag> newTags = updatedTagsName.stream().filter(tagName -> !existedTagNames.contains(tagName))
                    .map(tagName -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }).collect(Collectors.toList());

            Set<Tag> allTag = new HashSet<>();
            allTag.addAll(existedTags);
            allTag.addAll(newTags);

            blog.setTags(allTag);
        }

        if (updateBlogRequest.getBlogCategoryIds() != null && !updateBlogRequest.getBlogCategoryIds().isEmpty()) {
            Set<BlogCategory> categories = this.blogCategoryRepository
                    .findAllById(updateBlogRequest.getBlogCategoryIds()).stream().collect(Collectors.toSet());
            blog.setCategories(categories);
        }
    }
}
