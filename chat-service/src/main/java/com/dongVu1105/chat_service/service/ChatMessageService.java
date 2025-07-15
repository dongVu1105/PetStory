package com.dongVu1105.chat_service.service;

import com.dongVu1105.chat_service.dto.request.ChatMessageRequest;
import com.dongVu1105.chat_service.dto.response.ChatMessageResponse;
import com.dongVu1105.chat_service.dto.response.ProfileResponse;
import com.dongVu1105.chat_service.entity.ChatMessage;
import com.dongVu1105.chat_service.entity.Conversation;
import com.dongVu1105.chat_service.entity.ParticipantInfo;
import com.dongVu1105.chat_service.exception.AppException;
import com.dongVu1105.chat_service.exception.ErrorCode;
import com.dongVu1105.chat_service.mapper.ChatMessageMapper;
import com.dongVu1105.chat_service.repository.ChatMessageRepository;
import com.dongVu1105.chat_service.repository.ConversationRepository;
import com.dongVu1105.chat_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {
    ChatMessageMapper chatMessageMapper;
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ProfileClient profileClient;

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

    public ChatMessageResponse create (ChatMessageRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        conversationRepository.findById(request.getConversationId()).orElseThrow(
                () -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipantInfos()
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
        return toChatMessageResponse(chatMessageRepository.save(chatMessage));
    }

    private ChatMessageResponse toChatMessageResponse (ChatMessage chatMessage){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatMessageResponse chatMessageResponse = chatMessageMapper.toChatMessageResponse(chatMessage);
        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getUserId()));
        return chatMessageResponse;
    }
}
