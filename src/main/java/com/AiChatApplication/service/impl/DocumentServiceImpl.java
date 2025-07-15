package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.DocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final VectorStore vectorStore;

    @Autowired
    public DocumentServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void ingestDocument(Resource resource) {
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();
        vectorStore.add(documents);
    }

    @Override
    public List<Document> searchSimilar(String query) {
        return vectorStore.similaritySearch(query);
    }
}
