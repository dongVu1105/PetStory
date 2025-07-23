package com.dongVu1105.post_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

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
