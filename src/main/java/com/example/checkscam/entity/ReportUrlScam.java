package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_url_scams")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportUrlScam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "url_scam_id", nullable = false)
    private UrlScam urlScam;
}
