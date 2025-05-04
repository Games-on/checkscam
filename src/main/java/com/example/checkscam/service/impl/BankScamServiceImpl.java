package com.example.checkscam.service.impl;

import com.example.checkscam.repository.BankScamRepository;
import com.example.checkscam.service.BankScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankScamServiceImpl implements BankScamService {
    private final BankScamRepository repository;
}
