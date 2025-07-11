package com.dongVu1105.identity_service.dto.request;

import com.dongVu1105.identity_service.validation.BirthDayConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 5, message = "INVALID_USERNAME")
    String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    boolean gender;
    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String email;
    String phoneNumber;
    @BirthDayConstraint(min = 16, message = "INVALID_BIRTH")
    LocalDate birthday;

}
