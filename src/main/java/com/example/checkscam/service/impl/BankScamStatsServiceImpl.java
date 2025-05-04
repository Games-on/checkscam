package com.example.checkscam.service.impl;

import com.example.checkscam.repository.BankScamStatsRepository;
import com.example.checkscam.service.BankScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankScamStatsServiceImpl implements BankScamStatsService {
    private final BankScamStatsRepository repository;
}
