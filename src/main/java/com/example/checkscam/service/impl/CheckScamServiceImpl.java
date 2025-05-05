package com.example.checkscam.service.impl;

import com.example.checkscam.component.GptClient;
import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.dto.request.DeepSeekRequest;
import com.example.checkscam.service.*;
import com.example.checkscam.service.deepseek.DeepSeekService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckScamServiceImpl implements CheckScamService {
    private final ScamStatsService scamStatsService;
    private final EntityManager manager;
    private final GptClient gptClient;
    private final DeepSeekService deepSeekService;

    @Override
    public Object checkScam(CheckScamRequestDto requestDto) {
        Object infoScam = null;
        switch (requestDto.getType()){
            case SDT -> {
                infoScam = scamStatsService.getPhoneScamStatsInfo(requestDto.getInfo());
            }
            case STK -> {
                infoScam = scamStatsService.getBankScamStatsInfo(requestDto.getInfo());
            }
            case URL -> {
                infoScam = scamStatsService.getUrlScamStatsInfo(requestDto.getInfo());
            }
        }
        log.info( infoScam == null ? "khong co thong tin" : infoScam.toString());
        return gptClient.sendToGpt(infoScam == null ? null : infoScam.toString());
//        DeepSeekRequest deepSeekRequest = new DeepSeekRequest();
//        deepSeekRequest.setModel("deepseek-chat");
//        deepSeekRequest.setMax_tokens(100);
//        DeepSeekRequest.Message message = new DeepSeekRequest.Message();
//        message.setRole("user");
//        message.setContent("hom nay ban the nao"); // Nội dung câu hỏi của người dùng
//
//        deepSeekRequest.setMessages(List.of(message)); // Thêm vào danh sách messages
//
//        return deepSeekService.generateText(deepSeekRequest);
    }
}
