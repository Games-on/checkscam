package com.example.checkscam.service;

import com.example.checkscam.repository.projection.UrlScamStatsInfo;

public interface UrlScamStatsService {
    UrlScamStatsInfo getUrlScamStatsInfo(String url);
}
