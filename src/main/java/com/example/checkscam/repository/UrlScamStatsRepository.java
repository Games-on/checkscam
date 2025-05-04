package com.example.checkscam.repository;

import com.example.checkscam.entity.UrlScamStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlScamStatsRepository extends JpaRepository<UrlScamStats, Long> {
}