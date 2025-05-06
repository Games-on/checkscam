package com.example.checkscam.service.impl;

import com.example.checkscam.dto.PhoneScamDto;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.PhoneScamRepository;
import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhoneScamServiceImpl implements PhoneScamService {
    private final PhoneScamRepository repository;
    private final PhoneScamStatsRepository phoneScamStatsRepository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;

    @Override
    @Transactional
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {

        PhoneScam phoneScam = repository.findByPhoneNumber(report.getInfo());

        if (phoneScam == null) {
            phoneScam = new PhoneScam();
            phoneScam.setPhoneNumber(report.getInfo());
            phoneScam.setDescription(report.getInfoDescription());
            phoneScam = repository.save(phoneScam);
        }

        ScamStatsDto statsDto = (phoneScam.getStats() != null)
                ? new ScamStatsDto(phoneScam.getStats())
                : new ScamStatsDto();

        ScamStatsDto calculated = scamStatsService.handleStats(statsDto, report, idScamType);

        PhoneScamStats stats = phoneScam.getStats();
        if (stats == null) {
            stats = new PhoneScamStats(calculated);
            stats.setPhoneScam(phoneScam);
        } else {
            stats.apply(calculated);
        }

        stats = phoneScamStatsRepository.save(stats);
        phoneScam.setStats(stats);
        repository.save(phoneScam);
    }


}
