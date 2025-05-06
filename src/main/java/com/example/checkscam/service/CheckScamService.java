package com.example.checkscam.service;

import com.example.checkscam.dto.request.CheckScamRequestDto;

public interface CheckScamService {
    Object checkScam(CheckScamRequestDto requestDto);
}
