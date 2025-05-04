package com.example.checkscam.service;

import com.example.checkscam.repository.projection.PhoneScamStatsInfo;

public interface PhoneScamStatsService {
    PhoneScamStatsInfo getPhoneScamStatsInfo(String phoneNumber);
}
