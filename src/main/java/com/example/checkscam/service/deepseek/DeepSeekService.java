package com.example.checkscam.service.deepseek;

import com.example.checkscam.dto.request.DeepSeekRequest;
import com.example.checkscam.dto.response.DeepSeekResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekService {
    @Value("${deepseek.api.url}") // https://api.deepseek.com/v1/chat/completions
    private String apiUrl;

    @Value("${deepseek.api.key}") // API Key (nếu có)
    private String apiKey;

    private final RestTemplate restTemplate;

    public DeepSeekService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DeepSeekResponse generateText(DeepSeekRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<DeepSeekRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<DeepSeekResponse> response =
                restTemplate.exchange(
                        apiUrl,
                        HttpMethod.POST,
                        entity,
                        DeepSeekResponse.class
                );

        return response.getBody();
    }
}
