package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toCommentResponse (Comment comment);
    Comment toComment (CommentRequest request);
}
