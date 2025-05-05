package com.example.checkscam.service;

import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.exception.CheckScamException;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto request);

    ReportResponseDto updateReport(Long id, ReportRequestDto request) throws CheckScamException;

    ReportResponseDto handleReport(HandleReportRequestDto requestDto) throws CheckScamException;
}
