package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
