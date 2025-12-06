package com.ptit.thesis.smartrecruit.repository;

import com.ptit.thesis.smartrecruit.entity.Comment;
import com.ptit.thesis.smartrecruit.enums.CommentableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByCommentableIdAndCommentableType(Long id, CommentableType type);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);
}
