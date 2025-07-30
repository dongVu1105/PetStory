package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.request.ProfileUpdationRequest;
import com.dongVu1105.profile_service.dto.request.SearchRequest;
import com.dongVu1105.profile_service.dto.response.ProfileResponse;
import com.dongVu1105.profile_service.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping("/updateMyProfile")
    ApiResponse<ProfileResponse> update (@RequestBody @Valid ProfileUpdationRequest request){
        return ApiResponse.<ProfileResponse>builder().result(profileService.updateMyProfile(request)).build();
    }

    @GetMapping("/getAll")
    ApiResponse<List<ProfileResponse>> getAllProfile (){
        return ApiResponse.<List<ProfileResponse>>builder().result(profileService.getAllProfile()).build();
    }

    @GetMapping("/getMyProfile")
    ApiResponse<ProfileResponse> getMyProfile (){
        return ApiResponse.<ProfileResponse>builder().result(profileService.getMyProfile()).build();
    }

    @PutMapping("/avatar")
    ApiResponse<ProfileResponse> updateAvatar (@RequestParam("file") MultipartFile file){
        return ApiResponse.<ProfileResponse>builder().result(profileService.updateAvatar(file)).build();
    }

    @GetMapping("/search")
    ApiResponse<List<ProfileResponse>> search (@RequestBody SearchRequest request){
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.search(request))
                .build();
    }


}
