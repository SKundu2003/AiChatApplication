package com.AiChatApplication.controller;

import com.AiChatApplication.service.ChatService;
import com.AiChatApplication.service.ImageService;
import com.AiChatApplication.service.OllamaService;
import com.AiChatApplication.service.impl.OllamaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gen-ai")
public class GenAiController {
    private final ChatService chatService;
    private final ImageService imageService;
    private final OllamaService ollamaService;

    @Autowired
    public GenAiController(ChatService chatService, ImageService imageService,
                           OllamaServiceImpl ollamaService) {
        this.chatService = chatService;
        this.imageService = imageService;
        this.ollamaService = ollamaService;
    }

    @RequestMapping("/chat-model")
    public String chat(@RequestParam String message) {
        return chatService.generateResponseOptions(message);
    }

    @RequestMapping("/image")
    public String image(@RequestParam String prompt) {
        return imageService.generateImage(prompt);
    }


    //using chat client
    @RequestMapping("/chat-client")
    public String chatClient(@RequestParam String message) {
        return chatService.chatClient(message);
    }

    @PostMapping("/chat-local/{conversationId}")
    public String chat(@PathVariable String conversationId, @RequestBody String message) {
        return ollamaService.chat(message, conversationId);
    }
}
