package com.dongVu1105.notification_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String userId;
    String username;
    String content;
    String mediaUrl;
    String formatedCreateDate;
    Instant createdDate;
    Instant modifiedDate;
}
