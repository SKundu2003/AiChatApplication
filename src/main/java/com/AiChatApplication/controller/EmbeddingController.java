package com.AiChatApplication.controller;

import com.AiChatApplication.service.OllamaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/embeddings")
public class EmbeddingController {

    private final OllamaService ollamaService;

    public EmbeddingController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/get")
    public Map<String, Object> getEmbedding(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }
        float[] embedding = ollamaService.getEmbedding(text);
        
        // Convert float[] to List<Float> for JSON serialization
        List<Float> embeddingList = new ArrayList<>();
        for (float value : embedding) {
            embeddingList.add(value);
        }
                
        return Map.of(
            "text", text,
            "embedding_size", embedding.length,
            "embedding_vector", embeddingList
        );
    }

    @PostMapping("/similarity")
    public Map<String, Object> calculateSimilarity(@RequestBody Map<String, String> request) {
        String text1 = request.get("text1");
        String text2 = request.get("text2");
        
        if (text1 == null || text2 == null || text1.trim().isEmpty() || text2.trim().isEmpty()) {
            throw new IllegalArgumentException("Both text1 and text2 are required");
        }
        
        double similarity = ollamaService.calculateSimilarity(text1, text2);
        
        return Map.of(
            "text1", text1,
            "text2", text2,
            "similarity_percentage", String.format("%.2f%%", similarity)
        );
    }
}
