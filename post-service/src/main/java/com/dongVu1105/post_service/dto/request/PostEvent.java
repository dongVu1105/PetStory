package com.dongVu1105.post_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostEvent {
    String subject;
    String userId;
    String username;
    String createdDate;
}
