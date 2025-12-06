package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateCommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import java.util.List;

public interface CommentService {
    CommentResponse createCommentBlog(Long blogId, CommentRequest commentRequest);

    List<CommentResponse> getCommentsOfBlog(Long blogId);

    CommentResponse updateComment(Long id, UpdateCommentRequest updateCommentRequest);

    void deleteComment(Long id);
}
