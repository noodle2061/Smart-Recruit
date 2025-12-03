package com.ptit.thesis.smartrecruit.service.impl;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.request.UpdateCommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import com.ptit.thesis.smartrecruit.repository.CommentRepository;
import com.ptit.thesis.smartrecruit.service.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    @Override
    public CommentResponse createCommentBlog(CommentRequest commentRequest) {
        //
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest updateCommentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateComment'");
    }

}
