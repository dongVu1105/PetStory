package com.dongVu1105.chat_service.service;

import com.dongVu1105.chat_service.dto.request.ConversationRequest;
import com.dongVu1105.chat_service.dto.response.ConversationResponse;
import com.dongVu1105.chat_service.dto.response.ProfileResponse;
import com.dongVu1105.chat_service.entity.Conversation;
import com.dongVu1105.chat_service.entity.ParticipantInfo;
import com.dongVu1105.chat_service.exception.AppException;
import com.dongVu1105.chat_service.exception.ErrorCode;
import com.dongVu1105.chat_service.mapper.ConversationMapper;
import com.dongVu1105.chat_service.repository.ConversationRepository;
import com.dongVu1105.chat_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    ProfileClient profileClient;

    public List<ConversationResponse> myConversation (){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepository.findAllByParticipantInfosContainsUserId(userId);
        return conversations.stream().map(this::toConversationResponse).toList();

    }

    public ConversationResponse create (ConversationRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String participantId = request.getParticipantIds().getFirst();

        List<String> participantIds = new ArrayList<>();
        participantIds.add(userId);
        participantIds.add(participantId);

        participantIds = participantIds.stream().sorted().toList();
        String conversationHash = generateParticipantHash(participantIds);
        Conversation conversation = conversationRepository.findAllByConversationHash(conversationHash).orElseGet(() -> {
            ProfileResponse myProfile = profileClient.getProfile(userId).getResult();
            ProfileResponse participantProfile = profileClient.getProfile(participantId).getResult();
            if(Objects.isNull(myProfile) || Objects.isNull(participantProfile)){
                throw new AppException(ErrorCode.GET_PROFILE_FAIL);
            }

            List<ParticipantInfo> participantInfos = List.of(
                    ParticipantInfo.builder()
                            .userId(myProfile.getUserId())
                            .username(myProfile.getUsername())
                            .firstName(myProfile.getFirstName())
                            .lastName(myProfile.getLastName())
                            .gender(myProfile.isGender())
                            .avatar(myProfile.getAvatar())
                            .build(),
                    ParticipantInfo.builder()
                            .userId(participantProfile.getUserId())
                            .username(participantProfile.getUsername())
                            .firstName(participantProfile.getFirstName())
                            .lastName(participantProfile.getLastName())
                            .gender(participantProfile.isGender())
                            .avatar(participantProfile.getAvatar())
                            .build());
            Conversation newConversation = Conversation.builder()
                    .type(request.getType())
                    .participantInfos(participantInfos)
                    .conversationHash(conversationHash)
                    .createdDate(Instant.now())
                    .modifiedDate(Instant.now())
                    .build();
            return conversationRepository.save(newConversation);
        });
    return toConversationResponse(conversation);
    }

    private ConversationResponse toConversationResponse (Conversation conversation){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);
        conversationResponse.getParticipantInfos().stream()
                .filter(participantInfo -> !userId.equals(participantInfo.getUserId()))
                .findFirst()
                .ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getUsername());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });
        return conversationResponse;
    }

    private String generateParticipantHash (List<String> participantIds){
        StringJoiner stringJoiner = new StringJoiner("_");
        participantIds.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
