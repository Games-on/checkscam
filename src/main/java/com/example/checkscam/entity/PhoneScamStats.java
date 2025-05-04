package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "phone_scam_stats",
        indexes = {
                @Index(name = "idx_phone_cnt", columnList = "verified_count desc"),
                @Index(name = "idx_phone_last", columnList = "last_report_at desc")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneScamStats {

    @Id
    @Column(name = "phone_scam_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "phone_scam_id")
    private PhoneScam phoneScam;

    @Column(name = "verified_count", nullable = false)
    private Integer verifiedCount = 0;

    @Column(name = "reasons_json", columnDefinition = "json", nullable = false)
    private String reasonsJson;

    @Column(name = "last_report_at")
    private LocalDateTime lastReportAt;
}