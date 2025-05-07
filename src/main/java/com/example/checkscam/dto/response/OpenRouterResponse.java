package com.example.checkscam.dto.response;

import com.example.checkscam.dto.OpenRouterMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenRouterResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    @Setter
    @Getter
    @Data
    public static class Choice {
        private int index;
        private OpenRouterMessage message;
    }
}
