package com.example.checkscam.dto.response;

import com.example.checkscam.dto.BankScamDto;
import com.example.checkscam.dto.PhoneScamDto;
import com.example.checkscam.dto.UrlScamDto;
import com.example.checkscam.entity.AttachmentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
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
