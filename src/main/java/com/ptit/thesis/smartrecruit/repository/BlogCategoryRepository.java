package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptit.thesis.smartrecruit.entity.BlogCategory;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long>{
        List<BlogCategory> findAllByNameIn(List<String> names);
}
