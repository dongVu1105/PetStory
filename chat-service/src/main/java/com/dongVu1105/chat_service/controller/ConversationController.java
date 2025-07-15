package com.dongVu1105.chat_service.controller;

import com.dongVu1105.chat_service.dto.ApiResponse;
import com.dongVu1105.chat_service.dto.request.ConversationRequest;
import com.dongVu1105.chat_service.dto.response.ConversationResponse;
import com.dongVu1105.chat_service.service.ConversationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversation")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/create")
    public ApiResponse<ConversationResponse> create (@RequestBody @Valid ConversationRequest request){
        return ApiResponse.<ConversationResponse>builder().result(conversationService.create(request)).build();
    }

    @GetMapping("/my-conversations")
    public ApiResponse<List<ConversationResponse>> myConversation (){
        return ApiResponse.<List<ConversationResponse>>builder().result(conversationService.myConversation()).build();
    }
}
