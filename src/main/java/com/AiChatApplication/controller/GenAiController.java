package com.AiChatApplication.controller;

import com.AiChatApplication.service.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gen-ai")
public class GenAiController {
    private final ChatService chatService;

    public GenAiController(ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatService.generateResponseOptions(message);
    }

}
