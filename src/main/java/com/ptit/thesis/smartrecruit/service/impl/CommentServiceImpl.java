package com.ptit.thesis.smartrecruit.service.impl;

import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateCommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import com.ptit.thesis.smartrecruit.entity.Comment;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.CommentableType;
import com.ptit.thesis.smartrecruit.mapper.CommentMapper;
import com.ptit.thesis.smartrecruit.repository.CommentRepository;
import com.ptit.thesis.smartrecruit.service.CommentService;
import com.ptit.thesis.smartrecruit.utils.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Override
    public CommentResponse createCommentBlog(Long blogId, CommentRequest commentRequest) {
        User currentUser = AuthUtil.getCurrentUser();
        Comment comment = commentMapper.toEntity(commentRequest);
        comment.setCommentableId(blogId);
        comment.setCommentableType(CommentableType.BLOG);
        comment.setUser(currentUser);

        if (commentRequest.getParentId() != null) {
            Comment parent = new Comment();
            parent.setId(commentRequest.getParentId());
            comment.setParent(parent);
        }

        Comment newComment = this.commentRepository.save(comment);
        return commentMapper.toResponse(newComment);
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest updateCommentRequest) {
        User user = AuthUtil.getCurrentUser();
        Comment comment = this.commentRepository
                .findByIdAndUserId(id, user.getId()).
                orElseThrow(() -> new IllegalArgumentException("Not found comment"));

        comment.setContent(updateCommentRequest.getContent());

        return this.commentMapper.toResponse(this.commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long id) {
        User user = AuthUtil.getCurrentUser();
        Comment comment = this.commentRepository
                .findByIdAndUserId(id, user.getId()).
                orElseThrow(() -> new IllegalArgumentException("Not found comment"));

        this.commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getCommentsOfBlog(Long blogId) {
        List<Comment> comments =
                this.commentRepository.findCommentsByCommentableIdAndCommentableType(blogId,
                        CommentableType.BLOG);
        return this.commentMapper.toListResponse(comments);
    }

}
