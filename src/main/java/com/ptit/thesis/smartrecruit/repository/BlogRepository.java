package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Blog;
import java.util.*;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {    
    Optional<Blog> findBySlug(String slug);
}
