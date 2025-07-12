package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.response.ProfileResponse;
import com.dongVu1105.profile_service.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    ProfileService profileService;

    @PostMapping("/internal/user")
    ApiResponse<ProfileResponse> create (@RequestBody ProfileCreationRequest request){
        return ApiResponse.<ProfileResponse>builder().result(profileService.create(request)).build();
    }

    @GetMapping("/internal/user/getProfile/{userId}")
    ApiResponse<ProfileResponse> getProfile (@PathVariable String userId){
        return ApiResponse.<ProfileResponse>builder().result(profileService.getProfileByUserId(userId)).build();
    }
}
