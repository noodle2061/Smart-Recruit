package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptit.thesis.smartrecruit.entity.JobCategory;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    List<JobCategory> findAll();
}
