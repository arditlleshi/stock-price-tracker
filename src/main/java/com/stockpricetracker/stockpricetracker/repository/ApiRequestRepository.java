package com.stockpricetracker.stockpricetracker.repository;

import com.stockpricetracker.stockpricetracker.model.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
