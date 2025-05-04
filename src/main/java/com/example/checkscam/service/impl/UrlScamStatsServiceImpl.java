package com.example.checkscam.service.impl;

import com.example.checkscam.repository.UrlScamStatsRepository;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;
import com.example.checkscam.service.UrlScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UrlScamStatsServiceImpl implements UrlScamStatsService {
    private final UrlScamStatsRepository repository;

    @Override
    public UrlScamStatsInfo getUrlScamStatsInfo(String url) {
        return repository.findByUrlScam(url);
    }
}
