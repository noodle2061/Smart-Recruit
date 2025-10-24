package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptit.thesis.smartrecruit.entity.JobCategory;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
}
