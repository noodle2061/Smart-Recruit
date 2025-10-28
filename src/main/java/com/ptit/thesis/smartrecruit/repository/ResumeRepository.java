package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    
}
