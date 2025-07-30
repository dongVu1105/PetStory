package com.dongVu1105.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowResponse {
    String id;
    String followingId;
    String followerId;
    boolean isFollow;
    String createdDate;
}
