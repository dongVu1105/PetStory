package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping("/create")
    public ApiResponse<PostResponse> createPost (@RequestBody PostRequest request){
        return ApiResponse.<PostResponse>builder().result(postService.createPost(request)).build();
    }

    @GetMapping("/my-posts")
    public ApiResponse<List<PostResponse>> getMyPost (){
        return ApiResponse.<List<PostResponse>>builder().result(postService.getMyPost()).build();
    }
}
