package com.example.checkscam.dto;

import com.example.checkscam.entity.AttachmentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private String info;
    private String description;
    private Integer status;
    private String emailAuthorReport;
    private LocalDateTime dateReport;
    private PhoneScamDto phoneScamDto;
    private BankScamDto bankScamDto;
    private UrlScamDto urlScamDto;
    private AttachmentDto attachmentDto;
}