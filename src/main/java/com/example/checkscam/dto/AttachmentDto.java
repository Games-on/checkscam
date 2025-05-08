package com.example.checkscam.dto;

import com.example.checkscam.entity.NewsDto;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AttachmentDto {
    private Long id;
    private String url;
    private ReportDto report;
    private NewsDto news;
}