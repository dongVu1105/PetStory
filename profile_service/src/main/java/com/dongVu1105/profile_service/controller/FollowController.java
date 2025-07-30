package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.FollowRequest;
import com.dongVu1105.profile_service.dto.response.FollowResponse;
import com.dongVu1105.profile_service.service.FollowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowController {
    FollowService followService;

    @PostMapping
    public ApiResponse<FollowResponse> followOrUnfollow (@RequestBody FollowRequest request){
        return ApiResponse.<FollowResponse>builder().result(followService.followOrUnfollow(request)).build();
    }

    @GetMapping("/my-followings")
    public ApiResponse<List<FollowResponse>> findAllMyFollowings (){
        return ApiResponse.<List<FollowResponse>>builder().result(followService.findAllMyFollowings()).build();
    }

    @GetMapping("/my-followers")
    public ApiResponse<List<FollowResponse>> findAllMyFollowers (){
        return ApiResponse.<List<FollowResponse>>builder().result(followService.findAllMyFollowers()).build();
    }
}
