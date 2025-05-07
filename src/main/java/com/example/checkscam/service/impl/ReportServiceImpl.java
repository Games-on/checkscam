package com.example.checkscam.service.impl;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.constant.ScamInfoType;
import com.example.checkscam.constant.StatusReportEnum;
import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.ReportRepository;
import com.example.checkscam.repository.projection.ReportInfo;
import com.example.checkscam.service.BankScamService;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ReportService;
import com.example.checkscam.service.UrlScamService;
import com.example.checkscam.service.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final ReportMapper mapper;
    private final BankScamService bankScamService;
    private final PhoneScamService phoneScamService;
    private final UrlScamService urlScamService;

    @Override
    public ReportResponseDto createReport(ReportRequestDto request) {
        Report report = mapper.requestToEntity(request);
        report.setDateReport(LocalDateTime.now());
        report.setStatus(StatusReportEnum.PENDING.getType());
        return mapper.entityToResponse(repository.save(report));
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        log.info("Get all reports");
        List<Report> reports = repository.findAll();
        return reports.stream()
                .map(mapper::entityToResponse)
                .toList();
    }

    @Override
    public ReportResponseDto updateReport(Long id, ReportRequestDto request) throws CheckScamException {
        log.info("Update report with id {}", id);
        Report report = repository.findById(id).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        mapper.setValue(request, report);
        return mapper.entityToResponse(repository.save(report));
    }

    @Override
    public ReportResponseDto getById(Long id) throws CheckScamException {
        log.info("Get report with id {}", id);
        Report report = repository.findById(id).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        return mapper.entityToResponse(report);
    }

    @Override
    public List<ReportInfo> findTop10RepeatedInfoByType(Integer type) {
        return repository.findTop10RepeatedInfoByType(type);
    }

    @Override
    public ReportResponseDto handleReport(HandleReportRequestDto requestDto) throws CheckScamException {
        log.info("Handle report with id {}", requestDto.getIdReport());
        Report report = repository.findById(requestDto.getIdReport()).orElseThrow(
                () -> new CheckScamException(ErrorCodeEnum.NOT_FOUND));
        report.setStatus(requestDto.getStatus());
        report.setIdScamTypeAfterHandle(requestDto.getIdScamTypeAfterHandle());

        if (StatusReportEnum.APPROVED.getType().equals(requestDto.getStatus())) {
            if (ScamInfoType.STK.getType().equals(report.getType())) {
                bankScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
            if (ScamInfoType.SDT.getType().equals(report.getType())) {
                phoneScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
            if (ScamInfoType.URL.getType().equals(report.getType())) {
                urlScamService.handleAfterReport(report, requestDto.getIdScamTypeAfterHandle());
            }
        }

        return mapper.entityToResponse(repository.save(report));
    }

}
