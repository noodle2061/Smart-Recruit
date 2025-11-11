package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ptit.thesis.smartrecruit.dto.common.TagDTO;
import com.ptit.thesis.smartrecruit.entity.BlogCategory;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {
    List<BlogCategory> findAllByNameIn(List<String> names);

    @Query(value = """
            SELECT tags.id, tags.name
            FROM blog_tags
            LEFT JOIN tags ON blog_tags.tag_id = tags.id
            GROUP BY tags.id
            ORDER BY COUNT(*) DESC
        """, nativeQuery = true)
    List<TagDTO> findPopularTags();
}
