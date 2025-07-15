package com.dongVu1105.chat_service.mapper;

import com.dongVu1105.chat_service.dto.request.ChatMessageRequest;
import com.dongVu1105.chat_service.dto.response.ChatMessageResponse;
import com.dongVu1105.chat_service.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessage toChatMessage (ChatMessageRequest request);
    ChatMessageResponse toChatMessageResponse (ChatMessage chatMessage);
}
