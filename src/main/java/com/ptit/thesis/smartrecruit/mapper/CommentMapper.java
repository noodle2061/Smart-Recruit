package com.ptit.thesis.smartrecruit.mapper;

import com.ptit.thesis.smartrecruit.dto.request.CommentRequest;
import com.ptit.thesis.smartrecruit.dto.response.CommentResponse;
import com.ptit.thesis.smartrecruit.entity.Comment;
import com.ptit.thesis.smartrecruit.service.impl.AuthServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CommentMapper {
    @Autowired
    AuthServiceImpl authServiceImpl;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commentableId", ignore = true)
    @Mapping(target = "commentableType", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "children", ignore = true)
    public abstract Comment toEntity(CommentRequest commentRequest);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "createdBy", ignore = true)
    public abstract CommentResponse toResponse(Comment comment);

    public List<CommentResponse> toListResponse(List<Comment> comments) {
        return comments.stream().map(comment -> {
            CommentResponse commentResponse = this.toResponse(comment);
            commentResponse.setCreatedBy(this.authServiceImpl.toUserResponse(comment.getUser()));
            return commentResponse;
        }).toList();
    }
}
