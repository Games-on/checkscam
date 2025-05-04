package com.example.checkscam.service;

import com.example.checkscam.repository.projection.BankScamStatsInfo;

public interface BankScamStatsService {
    BankScamStatsInfo getBankScamStatsInfo(String bankAccount);
}
