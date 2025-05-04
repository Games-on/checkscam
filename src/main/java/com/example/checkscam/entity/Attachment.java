package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments",
        indexes = @Index(name = "report_id", columnList = "report_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment extends BaseEntity {

    @Column(columnDefinition = "text", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;
}

