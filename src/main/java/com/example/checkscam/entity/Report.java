package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {

    @Column(columnDefinition = "text")
    private String info;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "type")
    private Integer type;

    @Column(name = "id_scam_type_before_handle")
    private Long idScamTypeAfterHandle;

    @Column(name = "email_author_report")
    private String emailAuthorReport;

    @Column(name = "reason")
    private String reason;

    @Column(name = "info_description")
    private String infoDescription;

    @Column(name = "date_report")
    private LocalDateTime dateReport;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments;
}

