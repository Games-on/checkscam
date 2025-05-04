package com.example.checkscam.dto.request;

import com.example.checkscam.dto.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GptRequestDto {
    private String model;
    private List<Message> messages;

    public GptRequestDto(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    // getters/setters
}
