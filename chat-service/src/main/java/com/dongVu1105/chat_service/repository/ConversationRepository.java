package com.dongVu1105.chat_service.repository;

import com.dongVu1105.chat_service.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findAllByConversationHash (String conversationHash);
    @Query("{'participantInfos.userId' : ?0}")
    List<Conversation> findAllByParticipantInfosContainsUserId (String userId);
}
