package com.dongVu1105.chat_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.chat_service.dto.request.ChatMessageRequest;
import com.dongVu1105.chat_service.dto.response.ChatMessageResponse;
import com.dongVu1105.chat_service.dto.response.ProfileResponse;
import com.dongVu1105.chat_service.entity.ChatMessage;
import com.dongVu1105.chat_service.entity.Conversation;
import com.dongVu1105.chat_service.entity.ParticipantInfo;
import com.dongVu1105.chat_service.entity.WebSocketSession;
import com.dongVu1105.chat_service.exception.AppException;
import com.dongVu1105.chat_service.exception.ErrorCode;
import com.dongVu1105.chat_service.mapper.ChatMessageMapper;
import com.dongVu1105.chat_service.repository.ChatMessageRepository;
import com.dongVu1105.chat_service.repository.ConversationRepository;
import com.dongVu1105.chat_service.repository.WebSocketSessionRepository;
import com.dongVu1105.chat_service.repository.httpclient.ProfileClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {
    ChatMessageMapper chatMessageMapper;
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    WebSocketSessionRepository webSocketSessionRepository;
    SocketIOServer socketIOServer;
    ObjectMapper objectMapper;

    public List<ChatMessageResponse> getChatMessage (String conversationId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        conversationRepository.findById(conversationId).orElseThrow(
                        () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipantInfos()
                .stream()
                .filter(participantInfo -> participantInfo.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByConversationIdOrderByCreatedDateDesc(conversationId);
        return chatMessages.stream().map(this::toChatMessageResponse).toList();
    }

    public ChatMessageResponse create (ChatMessageRequest request) throws JsonProcessingException{
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Conversation conversation = conversationRepository.findById(request.getConversationId()).orElseThrow(
                () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
        conversation.getParticipantInfos()
                .stream()
                .filter(participantInfo -> participantInfo.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        ProfileResponse myProfile = profileClient.getProfile(userId).getResult();
        if(Objects.isNull(myProfile)){
            throw new AppException(ErrorCode.GET_PROFILE_FAIL);
        }
        chatMessage.setSender(ParticipantInfo.builder()
                        .userId(myProfile.getUserId())
                        .username(myProfile.getUsername())
                        .firstName(myProfile.getFirstName())
                        .lastName(myProfile.getLastName())
                        .gender(myProfile.isGender())
                        .avatar(myProfile.getAvatar())
                        .build());
        chatMessage.setCreatedDate(Instant.now());
        ChatMessageResponse chatMessageResponse = toChatMessageResponse(chatMessageRepository.save(chatMessage));
        List<String> userIds = new ArrayList<>();
        for(ParticipantInfo participantInfo : conversation.getParticipantInfos()){
            userIds.add(participantInfo.getUserId());
        }
        List<WebSocketSession> webSocketSessionList = webSocketSessionRepository.findAllByUserIdIn(userIds);
        Map<String, WebSocketSession> lookup = new TreeMap<>();
        for (WebSocketSession webSocketSession : webSocketSessionList){
            lookup.put(webSocketSession.getSocketSessionId(), webSocketSession);
        }
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            WebSocketSession webSocketSession = lookup.get(socketIOClient.getSessionId().toString());
            if(Objects.nonNull(webSocketSession)){
                String message = null;
                try {
                    chatMessageResponse.setMe(webSocketSession.getUserId().equals(userId));
                    message = objectMapper.writeValueAsString(chatMessageResponse);
                    socketIOClient.sendEvent("message", message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return this.toChatMessageResponse(chatMessage);
    }

    private ChatMessageResponse toChatMessageResponse (ChatMessage chatMessage){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatMessageResponse chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);
        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getUserId()));
        return chatMessageResponse;
    }
}
