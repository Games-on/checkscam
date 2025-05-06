package com.example.checkscam.repository;

import com.example.checkscam.entity.Report;
import com.example.checkscam.repository.projection.ReportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query(value = """
            WITH ranked AS (
                SELECT
                    r.id,
                    r.info,
                    r.description,
                    r.status,
                    r.type,
                    r.id_scam_type_before_handle AS idScamTypeAfterHandle,
                    r.email_author_report       AS emailAuthorReport,
                    r.reason,
                    r.info_description          AS infoDescription,
                    r.date_report               AS dateReport,
            
                    COUNT(*) OVER (PARTITION BY r.info) AS quantity,
            
                    ROW_NUMBER() OVER (
                        PARTITION BY r.info
                        ORDER BY r.date_report DESC
                    ) AS rn
                FROM report r
                WHERE r.info IS NOT NULL
                  AND r.type = :typeParam           
            )
            SELECT
                id,
                quantity,
                info,
                description,
                status,
                type,
                idScamTypeAfterHandle,
                emailAuthorReport,
                reason,
                infoDescription,
                dateReport
            FROM ranked
            WHERE rn = 1                  
              AND quantity > 1            
            ORDER BY quantity DESC
            LIMIT 10                       
            """, nativeQuery = true)
    List<ReportInfo> findTop10RepeatedInfoByType(@Param("typeParam") int type);

}