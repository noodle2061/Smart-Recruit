package com.ptit.thesis.smartrecruit.mapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.ptit.thesis.smartrecruit.dto.request.PostJobRequest;
import com.ptit.thesis.smartrecruit.dto.response.JobDetailResponse;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.JobCategory;
import com.ptit.thesis.smartrecruit.entity.Tag;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.exception.InvalidFieldException;
import com.ptit.thesis.smartrecruit.repository.JobCategoryRepository;
import com.ptit.thesis.smartrecruit.repository.TagRepository;
import com.ptit.thesis.smartrecruit.utils.StringUtil;

@Mapper(componentModel = "spring",
        uses = {TagMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class JobMapper {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    JobCategoryRepository jobCategoryRepository;

    /**
     * chuyển doi tu PostJobRequest -> Job
     * <p>
     * cac field company, location phải tự xử lý tay sau khi dùng hàm này
     * @param dto
     * @return
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "company", ignore = true) // xử lý ở service
    @Mapping(target = "status", ignore = true) // xử lý ỏ affter mapping
    @Mapping(target = "isFeatured", ignore = true) // xử lý ở affter mapping
    @Mapping(target = "deleteAt", ignore = true) // xử lý ở affter mapping
    @Mapping(target = "location", ignore = true) // xử lý ở service
    @Mapping(target = "postedAt", ignore = true) // xử lý ở affter mapping
    @Mapping(target = "jobApplications", ignore = true)
    @Mapping(target = "jobCategories", ignore = true)  // xử lý ỏ affter mapping
    @Mapping(target = "savedJobs", ignore = true)
    @Mapping(target = "slug", ignore = true) // xử lý ở affter mapping
    @Mapping(target = "tags", ignore = true) // xử lý ở affter mapping
    @Mapping(target = "type", source = "jobType")
    abstract public Job toJobEntity(PostJobRequest dto);

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "jobType", source = "type")
    abstract public JobDetailResponse toJobDetailResponse(Job job);

    @AfterMapping
    void updateJobEntity(PostJobRequest dto, @MappingTarget Job job) { // status, isFeatured, postedAt, 
                                                                            // slug , tag, category 
        job.setIsFeatured(false);
        job.setPostedAt(LocalDateTime.now());
        job.setStatus(JobStatus.ACTIVE);


        // tag
        List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
        if (tags.size() != dto.getTagIds().size()) {
            throw new InvalidFieldException("Lack of tags. Please reload the page. (this situation happen because admin delete some tags)");
        }
        Set<Tag> tagSet = new HashSet<>(tags);
        job.setTags(tagSet);

        // category
        List<JobCategory> categories = jobCategoryRepository.findAllById(dto.getCategoryIds());
        if (categories.size() != dto.getCategoryIds().size()) {
            throw new InvalidFieldException("Lack of categories. Please reload the page. (this situation happen because admin delete some categories)");
        }
        Set<JobCategory> categorySet = new HashSet<>(categories);
        job.setJobCategories(categorySet);

        // slug
        String slug = StringUtil.generateSlug(dto.getTitle());
        job.setSlug(slug);
    }
}
