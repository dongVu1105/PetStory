package com.dongVu1105.notification_service.controller;


import com.dongVu1105.notification_service.dto.request.CommentEvent;
import com.dongVu1105.notification_service.dto.request.FollowEvent;
import com.dongVu1105.notification_service.dto.request.ReactEvent;
import com.dongVu1105.notification_service.service.CommentNotificationService;
import com.dongVu1105.notification_service.service.EmailService;
import com.dongVu1105.notification_service.service.FollowNotificationService;
import com.dongVu1105.notification_service.service.ReactNotificationService;
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
    ReactNotificationService reactNotificationService;
    CommentNotificationService commentNotificationService;
    FollowNotificationService followNotificationService;

//    @KafkaListener(topics = "notification-delivery")
//    public void listenNotificationDelivery (NotificationEvent notificationEvent){
//        System.out.println(notificationEvent.toString());
//        emailService.sendEmail(SendEmailRequest.builder()
//                .to(Recipient.builder()
//                        .email(notificationEvent.getRecipient())
//                        .build())
//                .subject(notificationEvent.getSubject())
//                .htmlContent(notificationEvent.getBody())
//                .build());
//    }

    @KafkaListener(topics = "react-notification")
    public void listenReactNotification(ReactEvent reactEvent){
        reactNotificationService.send(reactEvent);
    }

    @KafkaListener(topics = "comment-notification")
    public void listenCommentNotification (CommentEvent commentEvent){
        commentNotificationService.send(commentEvent);
    }

    @KafkaListener(topics = "follow-notification")
    public void listenFollowNotification (FollowEvent followEvent){
        followNotificationService.send(followEvent);
    }
}
