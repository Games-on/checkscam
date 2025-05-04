package com.example.checkscam.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankScamDto {
    private Long id;
    private String bankAccount;
    private String description;
}