package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.ReactRequest;
import com.dongVu1105.post_service.dto.response.ReactQuantityResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.repository.ReactRepository;
import com.dongVu1105.post_service.service.ReactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/react")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactController {
    ReactService reactService;

    @PostMapping
    public ApiResponse<ReactResponse> click (@RequestBody ReactRequest request){
        return ApiResponse.<ReactResponse>builder().result(reactService.click(request)).build();
    }

    @GetMapping("/find-all/{postId}")
    public ApiResponse<List<ReactResponse>> findAllByPostId (@PathVariable("postId") String postId){
        return ApiResponse.<List<ReactResponse>>builder().result(reactService.findAllByPostId(postId)).build();
    }

    @GetMapping("/quantity/{postId}")
    public ApiResponse<ReactQuantityResponse> reactQuantity (@PathVariable("postId") String postId){
        return ApiResponse.<ReactQuantityResponse>builder().result(reactService.countByPostId(postId)).build();
    }

}
