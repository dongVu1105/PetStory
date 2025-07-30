package com.dongVu1105.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
