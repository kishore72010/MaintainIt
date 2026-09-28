package com.example.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.maintenance.model.Machine;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}