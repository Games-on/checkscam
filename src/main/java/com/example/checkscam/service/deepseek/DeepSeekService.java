package com.example.checkscam.service.deepseek;

import com.example.checkscam.dto.request.OpenRouterRequest;
import com.example.checkscam.dto.response.OpenRouterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeepSeekService {
    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Value("${deepseek.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public DeepSeekService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenRouterResponse chatCompletions(OpenRouterRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!ObjectUtils.isEmpty(apiKey)) {
            headers.set("Authorization", "Bearer " + apiKey);
        } else {
            log.error("OpenRouter API Key is not configured!");
        }
        HttpEntity<OpenRouterRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<OpenRouterResponse> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.POST,
                            entity,
                            OpenRouterResponse.class
                    );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling OpenRouter API: {}", e.getMessage());
        }
        return null;
    }
}
