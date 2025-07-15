package com.dongVu1105.chat_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("conversation")
public class Conversation {
    @MongoId
    String id;
    String type;
    @Indexed(unique = true)
    String conversationHash;
    List<ParticipantInfo> participantInfos;
    Instant createdDate;
    Instant modifiedDate;
}
