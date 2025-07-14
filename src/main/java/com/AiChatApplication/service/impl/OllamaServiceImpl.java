package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.OllamaService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OllamaServiceImpl implements OllamaService {

    private final ChatClient chatClient;
    private final Map<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();

    public OllamaServiceImpl(@Qualifier("ollamaChatModel") OllamaChatModel ollamaChatModel) {
        this.chatClient = ChatClient.create(ollamaChatModel);
    }

    @Override
    public String chat(String message, String conversationId) {

        // Get or create conversation history
        List<Message> history = conversationHistory.computeIfAbsent(conversationId, k -> new ArrayList<>());
        
        // Create user message
        UserMessage userMessage = new UserMessage(message);
        history.add(userMessage);
        
        // Create prompt with conversation history
        Prompt prompt = new Prompt(history);
        
        // Get AI response
        ChatResponse chatResponse = chatClient.prompt(prompt).call()
                .chatResponse();
        String response = chatResponse.getResult().getOutput().getText();

        // Add AI response to history
        history.add(new org.springframework.ai.chat.messages.AssistantMessage(response));
        
        return response;
    }
}