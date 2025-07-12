package com.dongVu1105.post_service.repository.httpclient;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping("/internal/user/getProfile/{userId}")
    ApiResponse<ProfileResponse> getProfile (@PathVariable String userId);
}
