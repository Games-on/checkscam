package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "report")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report extends BaseEntity{
    @Column(name = "created_by")
    private Integer createdBy;

    private String info;

    private String description;

    @Column(name = "news_id")
    private Integer newsId;

    private Integer status;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportUrlScam> reportUrlScams;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportPhoneScam> reportPhoneScams;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<ReportBankScam> reportBankScams;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<Attachment> attachments;
}

