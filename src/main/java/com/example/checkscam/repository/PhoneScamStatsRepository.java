package com.example.checkscam.repository;

import com.example.checkscam.entity.PhoneScamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneScamStatsRepository extends JpaRepository<PhoneScamStats, Long> {
}