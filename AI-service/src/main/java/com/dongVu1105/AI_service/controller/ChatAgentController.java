package com.dongVu1105.AI_service.controller;

import com.dongVu1105.AI_service.dto.ApiResponse;
import com.dongVu1105.AI_service.dto.request.ChatAgentRequest;
import com.dongVu1105.AI_service.service.ChatAgentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatAgentController {
    ChatAgentService chatAgentService;

    @PostMapping
    public ApiResponse<String> chatOnlyText (@RequestBody ChatAgentRequest request){
        return ApiResponse.<String>builder().result(chatAgentService.chat(request)).build();
    }
}
