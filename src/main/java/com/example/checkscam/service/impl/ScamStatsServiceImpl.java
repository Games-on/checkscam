package com.example.checkscam.service.impl;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.dto.ReasonsJsonDto;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.entity.ScamTypes;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.BankScamStatsRepository;
import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.repository.UrlScamStatsRepository;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;
import com.example.checkscam.service.ScamStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScamStatsServiceImpl implements ScamStatsService {
    private final UrlScamStatsRepository urlRepository;
    private final PhoneScamStatsRepository phoneRepository;
    private final BankScamStatsRepository bankRepository;
    private final ScamTypesRepository scamTypesRepository;

    @Override
    public BankScamStatsInfo getBankScamStatsInfo(String bankAccount) {
        return bankRepository.findByBankAccount(bankAccount);
    }

    @Override
    public PhoneScamStatsInfo getPhoneScamStatsInfo(String phoneNumber) {
        return phoneRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UrlScamStatsInfo getUrlScamStatsInfo(String url) {
        return urlRepository.findByUrlScam(url);
    }

    @Override
    public ScamStatsDto handleStats(ScamStatsDto stats, Report report, Long idScamType) throws CheckScamException {
        stats.setVerifiedCount(stats.getVerifiedCount() + 1);
        stats.setLastReportAt(report.getDateReport());
        ReasonsJsonDto reasonsJsonDto = stats.getReasonsJson();
        ScamTypes scamType = scamTypesRepository.findById(idScamType).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND)
        );
        if (reasonsJsonDto == null) {
            reasonsJsonDto = new ReasonsJsonDto();
            ReasonsJsonDto.Reason reason = new ReasonsJsonDto.Reason();
            reason.setName(scamType.getName());
            reason.setQuantity(1);
            reasonsJsonDto.setReasons(List.of(reason));
        }else {
            List<ReasonsJsonDto.Reason> reasons = reasonsJsonDto.getReasons();
            boolean found = false;
            for (ReasonsJsonDto.Reason reason : reasons) {
                if (reason.isExitByName(scamType.getName())) {
                    reason.setQuantity(reason.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                ReasonsJsonDto.Reason reason = new ReasonsJsonDto.Reason();
                reason.setName(scamType.getName());
                reason.setQuantity(1);
                reasons.add(reason);
            }
        }
        stats.setReasonsJson(reasonsJsonDto);
        return stats;
    }
}
