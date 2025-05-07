package com.example.checkscam.dto;

import com.example.checkscam.entity.NewsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AttachmentDto {
    private Long id;
    private String url;
    private ReportDto report;
    private NewsDto news;
}