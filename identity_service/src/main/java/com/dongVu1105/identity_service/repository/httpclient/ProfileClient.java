package com.dongVu1105.identity_service.repository.httpclient;

import com.dongVu1105.identity_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.response.ProfileCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = AuthenticationRequestInterceptor.class)
public interface ProfileClient {
    @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileCreationResponse> create (@RequestBody ProfileCreationRequest request);
}
