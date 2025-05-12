package com.example.checkscam.repository;

import com.example.checkscam.dto.NewsDto;
import com.example.checkscam.dto.search.NewsSearchDto;
import com.example.checkscam.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}