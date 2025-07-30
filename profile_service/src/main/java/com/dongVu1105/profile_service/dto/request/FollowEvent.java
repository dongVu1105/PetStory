package com.dongVu1105.profile_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowEvent {
    String subject;
    String followingId;
    String followerId;
    boolean isFollow;
    String createdDate;
}
