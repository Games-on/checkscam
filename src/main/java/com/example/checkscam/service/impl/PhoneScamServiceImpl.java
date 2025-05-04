package com.example.checkscam.service.impl;

import com.example.checkscam.repository.PhoneScamRepository;
import com.example.checkscam.service.PhoneScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneScamServiceImpl implements PhoneScamService {
    private final PhoneScamRepository repository;
}
