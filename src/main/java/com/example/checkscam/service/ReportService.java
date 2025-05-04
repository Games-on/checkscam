package com.example.checkscam.service;

import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;

public interface ReportService {
    ReportResponseDto createReport(ReportRequestDto request);
}
