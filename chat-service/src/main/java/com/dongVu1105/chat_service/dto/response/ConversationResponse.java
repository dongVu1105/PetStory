package com.dongVu1105.chat_service.dto.response;

import com.dongVu1105.chat_service.entity.ParticipantInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    String type;
    String conversationHash;
    List<ParticipantInfo> participantInfos;
    String conversationName;
    String conversationAvatar;
    Instant createdDate;
    Instant modifiedDate;
}
