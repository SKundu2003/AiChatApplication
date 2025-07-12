package com.AiChatApplication.service;

import org.springframework.ai.chat.model.ChatResponse;

public interface ChatService {
    String chat(String message);

    String generateResponseOptions(String message);

    //using chat client instead of chat model
    String chatClient(String message);
}
