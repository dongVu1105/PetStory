package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> create (@RequestBody CommentRequest request){
        return ApiResponse.<CommentResponse>builder().result(commentService.create(request)).build();
    }

    @GetMapping("/{postId}")
    public ApiResponse<List<CommentResponse>> findAllByPostId (@PathVariable("postId") String postId){
        return ApiResponse.<List<CommentResponse>>builder().result(commentService.findAllByPostId(postId)).build();
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteById (@PathVariable("commentId") String commentId){
        commentService.deleteById(commentId);
        return ApiResponse.<Void>builder().build();
    }
}
