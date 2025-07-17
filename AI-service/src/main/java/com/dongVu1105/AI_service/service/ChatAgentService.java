package com.dongVu1105.AI_service.service;

import com.dongVu1105.AI_service.dto.request.ChatAgentRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatAgentService {
    private final ChatClient chatClient;

    public ChatAgentService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

//    public String chat (ChatAgentRequest request){
//        return chatClient.prompt(request.getMessage()).call().content();
//    }

    public String chat (ChatAgentRequest request){
        SystemMessage systemMessage = new SystemMessage("You are PetStory AI support");
        UserMessage userMessage = new UserMessage(request.getMessage());
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient.prompt(prompt).call().content();
    }


}
