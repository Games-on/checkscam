package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "url_scam")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlScam {
    @Id
    private Integer id;

    private String info;
    private String description;

    @OneToMany(mappedBy = "urlScam", cascade = CascadeType.ALL)
    private List<ReportUrlScam> reportUrlScams;
}
