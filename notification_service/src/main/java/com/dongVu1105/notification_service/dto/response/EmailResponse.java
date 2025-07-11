package com.dongVu1105.notification_service.dto.response;

import com.dongVu1105.notification_service.dto.request.Recipient;
import com.dongVu1105.notification_service.dto.request.Sender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailResponse {
    String messageId;

}
