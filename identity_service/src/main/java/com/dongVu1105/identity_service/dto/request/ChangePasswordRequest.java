package com.dongVu1105.identity_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 8, message = "INVALID_PASSWORD")
    String oldPassword;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String newPassword;
}
