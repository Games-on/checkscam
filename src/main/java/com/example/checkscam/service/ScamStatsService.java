package com.example.checkscam.service;

import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;

public interface ScamStatsService {
    BankScamStatsInfo getBankScamStatsInfo(String bankAccount);

    PhoneScamStatsInfo getPhoneScamStatsInfo(String phoneNumber);

    UrlScamStatsInfo getUrlScamStatsInfo(String url);

    ScamStatsDto handleStats(ScamStatsDto stats, Report report, Long idScamType) throws CheckScamException;
}
