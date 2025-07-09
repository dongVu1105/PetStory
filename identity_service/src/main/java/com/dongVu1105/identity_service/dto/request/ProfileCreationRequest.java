package com.dongVu1105.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userID;
    String firstName;
    String lastName;
    boolean gender;
    String email;
    String phoneNumber;
    LocalDate birthday;
}
