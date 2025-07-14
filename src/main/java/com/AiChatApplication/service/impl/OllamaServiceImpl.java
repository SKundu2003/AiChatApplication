package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.OllamaService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
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
    private final EmbeddingModel embeddingModel;
    private final Map<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();

    public OllamaServiceImpl(@Qualifier("ollamaChatModel") OllamaChatModel ollamaChatModel,
                             @Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.chatClient = ChatClient.create(ollamaChatModel);
        this.embeddingModel = embeddingModel;
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

    @Override
    public float[] getEmbedding(String text) {
        try {
            // Using ChatClient's embedding capabilities
            return embeddingModel.embed(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    @Override
    public double calculateSimilarity(String text1, String text2) {
        float[] embedding1 = getEmbedding(text1);
        float[] embedding2 = getEmbedding(text2);
        
        if (embedding1.length != embedding2.length) {
            throw new IllegalArgumentException("Embedding sizes do not match");
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += Math.pow(embedding1[i], 2);
            norm2 += Math.pow(embedding2[i], 2);
        }
        
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }
        
        // Cosine similarity ranges from -1 to 1, we'll normalize to 0-1 and convert to percentage
        double similarity = dotProduct / (norm1 * norm2);
        return (similarity + 1) / 2 * 100;  // Convert to 0-100% range
    }
}