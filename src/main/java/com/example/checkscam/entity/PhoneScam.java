package com.example.checkscam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "phone_scam")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneScam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String description;

    @OneToMany(mappedBy = "phoneScam", cascade = CascadeType.ALL)
    private List<ReportPhoneScam> reportPhoneScams;
}
