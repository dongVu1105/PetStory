package com.dongVu1105.notification_service.repository.httpclient;

import com.dongVu1105.notification_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.notification_service.dto.ApiResponse;
import com.dongVu1105.notification_service.dto.response.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "post-service",
        url = "${app.services.post.url}")
public interface PostClient {
    @GetMapping("/internal/content/{postId}")
    ApiResponse<PostResponse> findById (@PathVariable("postId") String postId);
}
