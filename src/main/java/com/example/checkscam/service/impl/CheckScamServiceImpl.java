package com.example.checkscam.service.impl;

import com.example.checkscam.component.GptClient;
import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.service.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckScamServiceImpl implements CheckScamService {
    private final PhoneScamStatsService phoneScamStatsService;
    private final BankScamStatsService bankScamStatsService;
    private final UrlScamStatsService urlScamStatsService;
    private final EntityManager manager;
    private final GptClient gptClient;

    @Override
    public String checkScam(CheckScamRequestDto requestDto) {
        Object infoScam = null;
        switch (requestDto.getType()){
            case SDT -> {
                infoScam = phoneScamStatsService.getPhoneScamStatsInfo(requestDto.getInfo());
            }
            case STK -> {
                infoScam = bankScamStatsService.getBankScamStatsInfo(requestDto.getInfo());
            }
            case URL -> {
                infoScam = urlScamStatsService.getUrlScamStatsInfo(requestDto.getInfo());
            }
        }
        log.info( infoScam == null ? "khong co thong tin" : infoScam.toString());
        return gptClient.sendToGpt(infoScam == null ? null : infoScam.toString());
    }
}
