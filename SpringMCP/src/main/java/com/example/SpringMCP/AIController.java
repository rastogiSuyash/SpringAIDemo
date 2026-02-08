package com.example.SpringMCP;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class AIController {

    private ChatClient chatClient;

    public AIController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider ) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();

    }

    @GetMapping("/chat")
    public String getAIResponse(@RequestParam String message){

        return chatClient.prompt(message).call().content();

    }
}
