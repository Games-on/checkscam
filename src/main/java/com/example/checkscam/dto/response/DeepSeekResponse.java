package com.example.checkscam.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekResponse {
    private String id;
    private String object;
    private String created;
    private String model;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private String text;
        private Integer index;
        private String finish_reason;
    }
}
