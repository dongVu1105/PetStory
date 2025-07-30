package com.dongVu1105.notification_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.notification_service.dto.request.PostEvent;
import com.dongVu1105.notification_service.dto.response.FollowResponse;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import com.dongVu1105.notification_service.repository.httpclient.ProfileClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostNotificationService {
    WebSocketSessionRepository webSocketSessionRepository;
    SocketIOServer socketIOServer;
    ProfileClient profileClient;
    ObjectMapper objectMapper;

    public void send (PostEvent postEvent){
        List<FollowResponse> followers = profileClient.findAllFollowerByUserId(postEvent.getUserId()).getResult();
        List<String> followerId = followers.stream().map(FollowResponse::getFollowerId).toList();
        List<WebSocketSession> webSocketSessionList = webSocketSessionRepository.findAllByUserIdIn(followerId);
        List<String> sessionIdList = webSocketSessionList.stream()
                .map(WebSocketSession::getSocketSessionId).toList();
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            if(sessionIdList.contains(socketIOClient.getSessionId().toString())){
                String noti = null;
                try {
                    noti = objectMapper.writeValueAsString(postEvent);
                    socketIOClient.sendEvent("post-noti", noti);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
