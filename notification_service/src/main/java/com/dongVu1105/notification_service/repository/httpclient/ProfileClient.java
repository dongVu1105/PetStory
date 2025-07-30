package com.dongVu1105.notification_service.repository.httpclient;

import com.dongVu1105.notification_service.dto.ApiResponse;
import com.dongVu1105.notification_service.dto.response.FollowResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile.url}")
public interface ProfileClient {
    @GetMapping("/internal/follow/followers/{userId}")
    ApiResponse<List<FollowResponse>> findAllFollowerByUserId (@PathVariable("userId") String userId);
}
