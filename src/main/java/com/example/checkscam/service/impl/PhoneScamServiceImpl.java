package com.example.checkscam.service.impl;

import com.example.checkscam.dto.PhoneScamDto;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.PhoneScamRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneScamServiceImpl implements PhoneScamService {
    private final PhoneScamRepository repository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;

    @Override
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {
        PhoneScam phoneScam = repository.findByPhoneNumber(report.getInfo());
        if (phoneScam == null) {
            phoneScam = new PhoneScam();
            phoneScam.setPhoneNumber(report.getInfo());
            phoneScam.setDescription(report.getInfoDescription());
            phoneScam.setStats(new PhoneScamStats());
        }
        phoneScam.setStats(
                new PhoneScamStats(
                        scamStatsService.handleStats(
                                new ScamStatsDto(
                                        phoneScam.getStats()), report, idScamType)));
        repository.save(phoneScam);

        PhoneScamDto.builder()
                .id(phoneScam.getId())
                .phoneNumber(phoneScam.getPhoneNumber())
                .description(phoneScam.getDescription())
                .build();

    }
}
