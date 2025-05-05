package com.example.checkscam.service.impl;

import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.dto.UrlScamDto;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.UrlScamRepository;
import com.example.checkscam.service.ScamStatsService;
import com.example.checkscam.service.UrlScamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlScamServiceImpl implements UrlScamService {
    private final UrlScamRepository repository;
    private final ScamStatsService scamStatsService;

    @Override
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {
        UrlScam urlScam = repository.findByInfo(report.getInfo());
        if (urlScam == null) {
            urlScam = new UrlScam();
            urlScam.setInfo(report.getInfo());
            urlScam.setDescription(report.getInfoDescription());
            urlScam.setStats(new UrlScamStats());
        }
        urlScam.setStats(
                new UrlScamStats(
                        scamStatsService.handleStats(
                                new ScamStatsDto(
                                        urlScam.getStats()), report, idScamType)));
        repository.save(urlScam);

        UrlScamDto.builder()
                .id(urlScam.getId())
                .info(urlScam.getInfo())
                .description(urlScam.getDescription())
                .build();

    }
}
