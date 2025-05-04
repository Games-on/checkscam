package com.example.checkscam.repository;

import com.example.checkscam.entity.UrlScamStats;
import com.example.checkscam.repository.projection.UrlScamStatsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlScamStatsRepository extends JpaRepository<UrlScamStats, Long> {

    @Query(
        value = """
            SELECT 
                scam.url AS url,
                stats.url_scam_id AS id,
                stats.last_report_at AS lastReportAt,
                stats.verified_count AS verifiedCount,
                stats.reasons_json AS reasonsJson
            FROM url_scam_stats AS stats
            JOIN url_scam AS scam ON stats.url_scam_id = scam.id
            WHERE scam.url = :scamUrl
            """, nativeQuery = true
    )
    UrlScamStatsInfo findByUrlScam(@Param("scamUrl") String scamUrl);
}