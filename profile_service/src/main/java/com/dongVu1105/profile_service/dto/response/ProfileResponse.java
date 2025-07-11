package com.dongVu1105.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String id;
    String userId;
    String firstName;
    String lastName;
    boolean gender;
    String email;
    String phoneNumber;
    LocalDate birthday;
}
