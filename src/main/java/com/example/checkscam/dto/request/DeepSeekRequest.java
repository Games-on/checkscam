package com.example.checkscam.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekRequest {
    private String model; // Ví dụ: "deepseek-chat"
    private List<Message> messages; // Thay thế trường 'prompt' bằng 'messages'
    private Integer max_tokens;

    @Data
    public static class Message {
        private String role; // "user", "system", hoặc "assistant"
        private String content; // Nội dung tin nhắn
    }
}
