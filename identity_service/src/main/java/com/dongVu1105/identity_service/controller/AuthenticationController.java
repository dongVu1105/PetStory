package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.AuthenticationRequest;
import com.dongVu1105.identity_service.dto.request.LogoutRequest;
import com.dongVu1105.identity_service.dto.request.RefreshTokenRequest;
import com.dongVu1105.identity_service.dto.response.AuthenticationResponse;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate (@RequestParam AuthenticationRequest request) throws AppException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout (@RequestParam LogoutRequest request) throws Exception {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> refreshToken (@RequestParam RefreshTokenRequest request) throws Exception {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }


}
