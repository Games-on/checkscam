package com.example.checkscam.repository;

import com.example.checkscam.entity.BankScamStats;
import com.example.checkscam.repository.projection.BankScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankScamStatsRepository extends JpaRepository<BankScamStats, Long> {

    @Query(value = """
        SELECT 
            scam.bank_account AS bankAccount,
            stats.bank_scam_id AS id,
            stats.last_report_at AS lastReportAt,
            stats.verified_count AS verifiedCount
        FROM bank_scam_stats AS stats
        JOIN bank_scam AS scam ON stats.bank_scam_id = scam.id
        WHERE scam.bank_account = :bankAccount
        """, nativeQuery = true)
    BankScamStatsInfo findByBankAccount(@Param("bankAccount") String bankAccount);
}