package com.dongVu1105.profile_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("follow")
public class Follow {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
    String followingId;
    String followerId;
    Instant createdDate;
}
