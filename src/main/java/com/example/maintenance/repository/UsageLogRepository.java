package com.example.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.maintenance.model.UsageLog;

public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {
}