package com.example.checkscam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReportRequestDto {
    private Long id;
    private String info;
    private String description;
    private Integer status;
    private String emailAuthorReport;
    private LocalDateTime dateReport;
}
