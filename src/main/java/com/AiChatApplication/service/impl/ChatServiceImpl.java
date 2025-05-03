package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.ChatService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatModel chatModel;


    @Autowired
    public ChatServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String chat(String message) {
        return chatModel.call(message);
    }

    @Override
    public String generateResponseOptions(String message) {
        ChatResponse chatResponse =  chatModel.call(
                new Prompt(
                        message,
                        OpenAiChatOptions.builder()
                                .temperature(0.4)
                                .build()
                ));

        return chatResponse.getResult().getOutput().getText();
    }
}
