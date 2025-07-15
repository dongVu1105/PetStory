package com.dongVu1105.chat_service.repository.httpclient;

import com.dongVu1105.chat_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.chat_service.dto.ApiResponse;
import com.dongVu1105.chat_service.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping("/internal/user/getProfile/{userId}")
    ApiResponse<ProfileResponse> getProfile (@PathVariable("userId") String userId);
}
