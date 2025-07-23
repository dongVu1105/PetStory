package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/content")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalPostController {
    PostService postService;

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> findById (@PathVariable("postId") String postId){
        return ApiResponse.<PostResponse>builder().result(postService.findPostById(postId)).build();
    }
}
