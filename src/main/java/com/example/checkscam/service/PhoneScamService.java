package com.example.checkscam.service;

import com.example.checkscam.entity.Report;
import com.example.checkscam.exception.CheckScamException;

public interface PhoneScamService {
    void handleAfterReport(Report report, Long idScamType) throws CheckScamException;
}
