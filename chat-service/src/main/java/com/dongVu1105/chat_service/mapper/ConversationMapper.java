package com.dongVu1105.chat_service.mapper;

import com.dongVu1105.chat_service.dto.response.ConversationResponse;
import com.dongVu1105.chat_service.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse (Conversation conversation);
}
