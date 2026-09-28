package com.example.maintenance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.maintenance.model.MaintenanceTask;

public interface MaintenanceTaskRepository
        extends JpaRepository<MaintenanceTask, Long> {

    boolean existsByMachineIdAndStatusNot(
            Long machineId,
            String status
    );

    List<MaintenanceTask> findByDueDateBeforeAndStatusNot(
            LocalDate date,
            String status
    );
}