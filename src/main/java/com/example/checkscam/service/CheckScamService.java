package com.example.checkscam.service;

import com.example.checkscam.dto.request.CheckScamRequestDto;

public interface CheckScamService {
    String checkScam(CheckScamRequestDto requestDto);
}
