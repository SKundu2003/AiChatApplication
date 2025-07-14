package com.AiChatApplication.service;

public interface OllamaService {
    String chat(String message, String conversationId);
    float[] getEmbedding(String text);
    double calculateSimilarity(String text1, String text2);
}
