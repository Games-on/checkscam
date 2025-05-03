package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_phone_scams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportPhoneScam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "phone_scam_id", nullable = false)
    private PhoneScam phoneScam;
}
