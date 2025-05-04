package com.example.checkscam.service.impl;

import com.example.checkscam.repository.UrlScamRepository;
import com.example.checkscam.service.UrlScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlScamServiceImpl implements UrlScamService {
    private final UrlScamRepository repository;
}
