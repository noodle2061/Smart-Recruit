package com.ptit.thesis.smartrecruit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    Optional<Blog> findBySlug(String slug);

    @Query("SELECT b FROM Blog as b WHERE b.slug = :slug AND b.author.id = :userId")
    Optional<Blog> findBlogBySlugAndUserId(@Param("slug") String slug, @Param("userId") Long userId);
}
