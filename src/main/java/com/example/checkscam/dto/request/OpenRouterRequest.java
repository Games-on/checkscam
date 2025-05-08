package com.example.checkscam.dto.request;

import com.example.checkscam.dto.OpenRouterMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class OpenRouterRequest {
    private String model;
    private List<OpenRouterMessage> messages;

    public OpenRouterRequest() {
    }

    public OpenRouterRequest(String model, List<OpenRouterMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

}
