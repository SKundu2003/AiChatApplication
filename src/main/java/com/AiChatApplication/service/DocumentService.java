package com.AiChatApplication.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;

import java.util.List;

public interface DocumentService {
    void ingestDocument(Resource resource);
    List<Document> searchSimilar(String query);

    String findAnswerFromDocumentViaRAG(String query);
}
