package com.example.checkscam.rest;

import com.example.checkscam.dto.request.HandleReportRequestDto;
import com.example.checkscam.dto.request.ReportRequestDto;
import com.example.checkscam.dto.response.ReportResponseDto;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.projection.ReportInfo;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class RestReportController {
    private final ReportService reportService;

    @PostMapping
    public CheckScamResponse<ReportResponseDto> createReport(@RequestBody ReportRequestDto request) {
        return new CheckScamResponse<>(reportService.createReport(request));
    }

    @PutMapping("/{id}")
    public CheckScamResponse<ReportResponseDto> updateReport(@PathVariable Long id,@RequestBody ReportRequestDto request) throws CheckScamException {
        return new CheckScamResponse<>(reportService.updateReport(id, request));
    }

    @PostMapping("/handle")
    public CheckScamResponse<ReportResponseDto> handleReport(@RequestBody HandleReportRequestDto request) throws CheckScamException {
        return new CheckScamResponse<>(reportService.handleReport(request));
    }

    @GetMapping("/{id}")
    public CheckScamResponse<ReportResponseDto> getById(@PathVariable Long id) throws CheckScamException {
        return new CheckScamResponse<>(reportService.getById(id));
    }

    @GetMapping("/top")
    public CheckScamResponse<List<ReportInfo>> getTopReportsByType(@RequestParam Integer type) {
        return new CheckScamResponse<>(reportService.findTop10RepeatedInfoByType(type));
    }
}
