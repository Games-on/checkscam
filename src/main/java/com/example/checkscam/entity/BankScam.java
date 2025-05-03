package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "bank_scam")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankScam{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bank_account")
    private String bankAccount;

    private String description;

    @OneToMany(mappedBy = "bankScam", cascade = CascadeType.ALL)
    private List<ReportBankScam> reportBankScams;
}
