package com.AiChatApplication.service.impl;

import com.AiChatApplication.service.ImageService;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageModel ImageModel;

    @Autowired
    public ImageServiceImpl(ImageModel ImageModel) {
        this.ImageModel = ImageModel;
    }

    @Override
    public String generateImage(String prompt) {
        ImageResponse response = ImageModel.call(
                new ImagePrompt(prompt)
        );
        return response.getResult().getOutput().getUrl();
    }

}
