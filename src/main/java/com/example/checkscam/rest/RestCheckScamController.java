package com.example.checkscam.rest;

import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.CheckScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/check-scam")
@RequiredArgsConstructor
public class RestCheckScamController {
    private final CheckScamService checkScamService;

    @PostMapping
    public CheckScamResponse<String> checkScam(@RequestBody @NotNull CheckScamRequestDto requestDto) {
        return new CheckScamResponse<>(this.checkScamService.checkScam(requestDto));
    }
}
