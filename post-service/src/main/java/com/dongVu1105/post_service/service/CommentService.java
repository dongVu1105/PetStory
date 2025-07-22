package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.entity.Comment;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.CommentMapper;
import com.dongVu1105.post_service.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    DateTimeFormatter dateTimeFormatter;

    public CommentResponse create (CommentRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = commentMapper.toComment(request);
        comment.setCreatedDate(Instant.now());
        comment.setUserId(userId);
        comment = commentRepository.save(comment);
        return toCommentResponse(comment);
    }

    public List<CommentResponse> findAllByPostId (String postId){
        return commentRepository.findAllByPostId(postId).stream().map(this::toCommentResponse).toList();
    }

    public void deleteById (String commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!comment.getUserId().equals(userId)){
            throw new AppException(ErrorCode.CANNOT_DELETE_COMMENT);
        }
        commentRepository.deleteById(commentId);
    }

    private CommentResponse toCommentResponse (Comment comment){
        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setCreatedDate(dateTimeFormatter.format(comment.getCreatedDate()));
        return commentResponse;
    }


}
