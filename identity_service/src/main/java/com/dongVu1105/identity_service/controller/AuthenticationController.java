package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.AuthenticationRequest;
import com.dongVu1105.identity_service.dto.request.IntrospectRequest;
import com.dongVu1105.identity_service.dto.request.LogoutRequest;
import com.dongVu1105.identity_service.dto.request.RefreshTokenRequest;
import com.dongVu1105.identity_service.dto.response.AuthenticationResponse;
import com.dongVu1105.identity_service.dto.response.IntrospectResponse;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request) throws AppException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request) throws AppException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout (@RequestBody LogoutRequest request) throws Exception {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> refreshToken (@RequestBody RefreshTokenRequest request) throws Exception {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }


}
