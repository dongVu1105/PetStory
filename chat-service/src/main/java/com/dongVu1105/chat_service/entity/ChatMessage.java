package com.dongVu1105.chat_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("chat_message")
public class ChatMessage {
    @MongoId
    String id;
    @Indexed
    String conversationId;
    String message;
    ParticipantInfo sender;
    @Indexed
    Instant createdDate;
}
