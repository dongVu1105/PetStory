package com.dongVu1105.chat_service.dto.response;

import com.dongVu1105.chat_service.entity.ParticipantInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageResponse {
    String id;
    String conversationId;
    String message;
    ParticipantInfo sender;
    boolean me;
    Instant createdDate;
}
