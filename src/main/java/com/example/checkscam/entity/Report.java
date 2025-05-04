package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report",
        indexes = {
                @Index(name = "idx_report_bank_scam", columnList = "bank_scam_id"),
                @Index(name = "idx_report_phone_scam", columnList = "phone_scam_id"),
                @Index(name = "idx_report_url_scam", columnList = "url_scam_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    @Column(columnDefinition = "text")
    private String info;

    @Column(columnDefinition = "text")
    private String description;

    private Integer status;                 // tuỳ enum

    @Column(name = "email_author_report")
    private String emailAuthorReport;

    @Column(name = "date_report")
    private LocalDateTime dateReport;

    /* --- Liên kết N‑1 tuỳ chọn --- */
    @ManyToOne
    @JoinColumn(name = "phone_scam_id")
    private PhoneScam phoneScam;

    @ManyToOne
    @JoinColumn(name = "bank_scam_id")
    private BankScam bankScam;

    @ManyToOne
    @JoinColumn(name = "url_scam_id")
    private UrlScam urlScam;

    /* attachments */
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;
}

