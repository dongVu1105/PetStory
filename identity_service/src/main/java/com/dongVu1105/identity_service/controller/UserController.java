package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> create (@RequestBody @Valid UserCreationRequest request) throws AppException {
        return ApiResponse.<UserResponse>builder().result(userService.create(request)).build();
    }

    @GetMapping("/getAll")
    public ApiResponse<List<UserResponse>> getUsers (){
        return ApiResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
    }
}
