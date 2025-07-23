package com.dongVu1105.notification_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.notification_service.dto.request.CommentEvent;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentNotificationService {
    WebSocketSessionRepository webSocketSessionRepository;
    SocketIOServer socketIOServer;
    ObjectMapper objectMapper;

    public void send (CommentEvent event){
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(event.getPostOwnerId());
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(event);
                        socketIOClient.sendEvent("comment-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
