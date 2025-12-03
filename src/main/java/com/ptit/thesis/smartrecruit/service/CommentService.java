package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateCommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;

public interface CommentService {
    public CommentResponse createCommentBlog(CommentRequest commentRequest);

    public CommentResponse updateComment(Long id, UpdateCommentRequest updateCommentRequest);
}
