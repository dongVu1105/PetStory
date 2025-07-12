package com.dongVu1105.notification_service.controller;


import com.dongVu1105.notification_service.dto.ApiResponse;
import com.dongVu1105.notification_service.dto.request.NotificationEvent;
import com.dongVu1105.notification_service.dto.request.Recipient;
import com.dongVu1105.notification_service.dto.request.SendEmailRequest;
import com.dongVu1105.notification_service.repository.httpclient.EmailClient;
import com.dongVu1105.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

//    @KafkaListener(topics = "notification-delivery")
//    public void listenNotificationDelivery (NotificationEvent notificationEvent){
//        emailService.sendEmail(SendEmailRequest.builder()
//                .to(Recipient.builder()
//                        .email(notificationEvent.getRecipient())
//                        .build())
//                .subject(notificationEvent.getSubject())
//                .htmlContent(notificationEvent.getBody())
//                .build());
//    }
}
