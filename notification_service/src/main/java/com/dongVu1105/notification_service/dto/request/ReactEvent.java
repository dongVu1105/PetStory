package com.dongVu1105.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactEvent {
    String subject;
    String postId;
    String userId;
    String postOwnerId;
    boolean isReact;
    long quantity;
    String createdDate;
}
