package com.project.daily.services;

import com.project.daily.model.ai.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlamaClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    // URL do ngrok
    private final String LLAMA_URL = "https://bb00a8e2cc55.ngrok-free.app/chat/completions";

    public String summarizeDaily(String prompt) {
        LlamaRequest request = new LlamaRequest();
        request.setMessages(List.of(
                new LlamaMessage("user", prompt)
        ));

        LlamaResponse response = restTemplate.postForObject(
                LLAMA_URL,
                request,
                LlamaResponse.class
        );

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "Erro: não foi possível gerar resumo.";
        }

        return response.getChoices().get(0).getMessage().getContent();
    }
}
