package com.example.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.maintenance.model.Technician;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
}