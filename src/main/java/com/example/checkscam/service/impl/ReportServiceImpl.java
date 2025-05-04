package com.example.checkscam.service.impl;

import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.entity.Report;
import com.example.checkscam.repository.ReportRepository;
import com.example.checkscam.service.ReportService;
import com.example.checkscam.service.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final ReportMapper mapper;

    @Override
    public ReportResponseDto createReport(ReportRequestDto request) {
        Report report = mapper.requestToEntity(request);
        return mapper.entityToResponse(repository.save(report));
    }

}
