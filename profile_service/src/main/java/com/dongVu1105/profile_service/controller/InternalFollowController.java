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
@RequestMapping("/internal/follow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalFollowController {
    FollowService followService;

    @GetMapping("/followers/{userId}")
    public ApiResponse<List<FollowResponse>> findAllFollowerByUserId (@PathVariable("userId") String userId){
        return ApiResponse.<List<FollowResponse>>builder().result(followService.findAllFollowerByFollowingId(userId)).build();
    }
}
