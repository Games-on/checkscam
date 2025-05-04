package com.example.checkscam.service.impl;

import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.service.PhoneScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneScamStatsServiceImpl implements PhoneScamStatsService {
    private final PhoneScamStatsRepository repository;
}
