package com.example.checkscam.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GptClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader("Content-Type", "application/json")
            .build();

    public String sendToGpt(String message) {
//        GptRequest request = new GptRequest("gpt-3.5-turbo",
//                List.of(new Message("user", message)));
//
//        GptResponse response = webClient.post()
//                .header("Authorization", "Bearer " + apiKey)
//                .body(Mono.just(request), GptRequest.class)
//                .retrieve()
//                .bodyToMono(GptResponse.class)
//                .block();
//
//        return response.getChoices().get(0).getMessage().getContent();
        return "gia lap call thanh cong GPT";
    }
}
