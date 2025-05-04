package com.example.checkscam.repository;

import com.example.checkscam.entity.PhoneScamStats;
import com.example.checkscam.repository.projection.PhoneScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneScamStatsRepository extends JpaRepository<PhoneScamStats, Long> {

    @Query(
        value = """
            SELECT 
                scam.phone_number AS phoneNumber,
                stats.phone_scam_id AS id,
                stats.last_report_at AS lastReportAt,
                stats.verified_count AS verifiedCount,
                stats.reasons_json AS reasonsJson
            FROM phone_scam_stats AS stats
            JOIN phone_scam AS scam ON stats.phone_scam_id = scam.id
            WHERE scam.phone_number = :phoneNumber
            """, nativeQuery = true
    )
    PhoneScamStatsInfo findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}