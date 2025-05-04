package com.example.checkscam.repository;

import com.example.checkscam.entity.BankScamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankScamStatsRepository extends JpaRepository<BankScamStats, Long> {
}