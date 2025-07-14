package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatModel chatModel;
    private final ChatClient chatClient;

    @Autowired
    public ChatServiceImpl(OpenAiChatModel openAiChatModel
//            ,ChatClient.Builder chatClientBuilder
    ) {
        //adding memory to the chat
//        this.chatClient = chatClientBuilder
//                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
//                .build();
        this.chatClient = ChatClient.create(openAiChatModel);
        this.chatModel = openAiChatModel;
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


    //using chat client instead of chat model
    @Override
    public String chatClient(String message) {
        ChatResponse chatResponse = chatClient.prompt(message)
                .call()
                .chatResponse();
        System.err.println(chatResponse.getMetadata().getUsage());
        System.err.println(chatResponse.getMetadata().getModel());
        return chatResponse.getResult().getOutput().getText();
    }
}
