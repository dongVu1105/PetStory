package com.dongVu1105.chat_service.controller;

import com.dongVu1105.chat_service.dto.ApiResponse;
import com.dongVu1105.chat_service.dto.request.ChatMessageRequest;
import com.dongVu1105.chat_service.dto.response.ChatMessageResponse;
import com.dongVu1105.chat_service.service.ChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageController {
    ChatMessageService chatMessageService;

    @PostMapping("/create")
    public ApiResponse<ChatMessageResponse> create (@RequestBody @Valid ChatMessageRequest request) throws JsonProcessingException {
        return ApiResponse.<ChatMessageResponse>builder().result(chatMessageService.create(request)).build();
    }

    @GetMapping
    public ApiResponse<List<ChatMessageResponse>> getMessages (@RequestParam("conversationId") String conversationId){
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .result(chatMessageService.getChatMessage(conversationId)).build();
    }
}
