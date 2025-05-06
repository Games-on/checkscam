package com.example.checkscam.service.impl;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.dto.BankScamDto;
import com.example.checkscam.dto.ReasonsJsonDto;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.BankScam;
import com.example.checkscam.entity.BankScamStats;
import com.example.checkscam.entity.Report;
import com.example.checkscam.entity.ScamTypes;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.BankScamRepository;
import com.example.checkscam.repository.BankScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.BankScamService;
import com.example.checkscam.service.ScamStatsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankScamServiceImpl implements BankScamService {
    private final BankScamRepository repository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;
    private final BankScamStatsRepository bankScamStatsRepository;

    @Transactional
    @Override
    public void handleAfterReport(Report report, Long idScamType)
            throws CheckScamException {

        BankScam bankScam = repository.findByBankAccount(report.getInfo());
        if (bankScam == null) {
            bankScam = new BankScam();
            bankScam.setBankAccount(report.getInfo());
            bankScam.setDescription(report.getInfoDescription());
            bankScam = repository.save(bankScam);
        }

        ScamStatsDto statsDto = (bankScam.getStats() != null)
                ? new ScamStatsDto(bankScam.getStats())
                : new ScamStatsDto();

        statsDto = scamStatsService.handleStats(statsDto, report, idScamType);

        BankScamStats statsEntity;
        if (bankScam.getStats() == null) {
            statsEntity = new BankScamStats(statsDto); // MapStruct
            statsEntity.setBankScam(bankScam);
        } else {
            statsEntity = bankScam.getStats();
            statsEntity.apply(statsDto);
        }

        statsEntity = bankScamStatsRepository.save(statsEntity);
        bankScam.setStats(statsEntity);
        repository.save(bankScam);
    }
}
