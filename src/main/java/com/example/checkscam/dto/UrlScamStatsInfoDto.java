package com.example.checkscam.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlScamStatsInfoDto {
    private Long id;
    private String urlScam;
    private Integer verifiedCount;
    private LocalDateTime lastReportAt;

    // Constructor matching the query
    public UrlScamStatsInfoDto(Long id, String urlScam, Integer verifiedCount, LocalDateTime lastReportAt) {
        this.id = id;
        this.urlScam = urlScam;
        this.verifiedCount = verifiedCount;
        this.lastReportAt = lastReportAt;
    }

    // Getters and setters (if needed)
}
