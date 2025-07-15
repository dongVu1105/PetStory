package com.dongVu1105.chat_service.repository.httpclient;

import com.dongVu1105.chat_service.dto.ApiResponse;
import com.dongVu1105.chat_service.dto.request.IntrospectRequest;
import com.dongVu1105.chat_service.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "identity-service", url = "${app.services.identity}")
public interface IdentityClient {
    @PostMapping("/auth/introspect")
    ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request);
}
