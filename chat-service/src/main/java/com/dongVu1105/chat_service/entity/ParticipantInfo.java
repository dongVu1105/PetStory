package com.dongVu1105.chat_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantInfo {
    String userId;
    String firstName;
    String lastName;
    boolean gender;
    String username;
    String avatar;
}
