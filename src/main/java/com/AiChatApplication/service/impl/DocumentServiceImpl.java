package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.DocumentService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final VectorStore vectorStore;

    private final ChatClient chatClient;

    @Autowired
    public DocumentServiceImpl(VectorStore vectorStore, @Qualifier("ollamaChatModel") OllamaChatModel ollamaChatModel) {
        this.vectorStore = vectorStore;
        this.chatClient = ChatClient.create(ollamaChatModel);
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

    @Override
    public String findAnswerFromDocumentViaRAG(String query) {
        Advisor advisor = (Advisor) new QuestionAnswerAdvisor(vectorStore);
        return chatClient.prompt()
                .advisors(advisor)   // available only in v1.0.0+
                .user(query)
                .call()
                .content();
    }
}
