package com.dongVu1105.notification_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.notification_service.dto.request.ReactEvent;
import com.dongVu1105.notification_service.dto.response.PostResponse;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import com.dongVu1105.notification_service.repository.httpclient.PostClient;
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
public class ReactNotificationService {
    SocketIOServer socketIOServer;
    WebSocketSessionRepository webSocketSessionRepository;
    ObjectMapper objectMapper;

    public void send (ReactEvent event){
        // sua lai chu bai viet
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(event.getPostOwnerId());
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(event);
                        socketIOClient.sendEvent("react-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
