package com.example.checkscam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
public class OpenRouterMessage {
    // Getters and Setters
    private String role;
    private String content;
    private String type;

    public OpenRouterMessage(String role, String content, String type) {
        this.role = role;
        this.content = content;
        this.type = type;
    }

}
