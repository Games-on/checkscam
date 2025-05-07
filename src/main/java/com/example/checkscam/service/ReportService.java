package com.example.checkscam.service;

import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.projection.ReportInfo;

import java.util.List;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto request);

    List<ReportResponseDto> getAllReports();

    ReportResponseDto updateReport(Long id, ReportRequestDto request) throws CheckScamException;

    ReportResponseDto getById(Long id) throws CheckScamException;

    List<ReportInfo> findTop10RepeatedInfoByType(Integer type);

    ReportResponseDto handleReport(HandleReportRequestDto requestDto) throws CheckScamException;
}
