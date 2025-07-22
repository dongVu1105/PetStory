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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/create")
    public ApiResponse<PostResponse> createPost(
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PostRequest request = mapper.readValue(requestJson, PostRequest.class);
        return ApiResponse.<PostResponse>builder().result(postService.createPost(request, file)).build();
    }

    @GetMapping("/my-posts")
    public ApiResponse<PageResponse<PostResponse>> getMyPost (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<PostResponse>>builder().result(postService.getMyPost(page, size)).build();
    }
}
