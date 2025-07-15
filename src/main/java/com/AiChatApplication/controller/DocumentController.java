package com.AiChatApplication.controller;

import com.AiChatApplication.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.document.Document;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DocumentController(DocumentService documentService, ResourceLoader resourceLoader) {
        this.documentService = documentService;
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/ingest")
    public String ingestDocuments() {
        Resource resource = resourceLoader.getResource("classpath:data.txt");
        documentService.ingestDocument(resource);
        return "Documents ingested successfully!";
    }

    @GetMapping("/search")
    public List<Document> searchSimilar(@RequestParam String query) {
        return documentService.searchSimilar(query);
    }
}
