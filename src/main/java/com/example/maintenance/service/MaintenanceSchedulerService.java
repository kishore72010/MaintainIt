package com.example.maintenance.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.maintenance.model.Machine;
import com.example.maintenance.model.MaintenanceTask;
import com.example.maintenance.repository.MachineRepository;
import com.example.maintenance.repository.MaintenanceTaskRepository;

@Service
public class MaintenanceSchedulerService {

    private final MachineRepository machineRepository;
    private final MaintenanceTaskRepository maintenanceTaskRepository;

    public MaintenanceSchedulerService(
            MachineRepository machineRepository,
            MaintenanceTaskRepository maintenanceTaskRepository) {

        this.machineRepository = machineRepository;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void checkCalendarMaintenance() {

        List<Machine> machines = machineRepository.findAll();

        for (Machine machine : machines) {

            if (!"DAYS".equalsIgnoreCase(machine.getIntervalType())) {
                continue;
            }

            if (machine.getLastMaintenanceDate() == null) {
                continue;
            }

            long daysPassed = ChronoUnit.DAYS.between(
                    machine.getLastMaintenanceDate(),
                    LocalDate.now()
            );

            double interval = machine.getMaintenanceInterval();

            if (interval <= 0) {
                continue;
            }

            if (daysPassed >= interval * 0.80) {

                boolean openTask =
                        maintenanceTaskRepository
                                .existsByMachineIdAndStatusNot(
                                        machine.getId(),
                                        "COMPLETED"
                                );

                if (!openTask) {

                    MaintenanceTask task = new MaintenanceTask();

                    task.setTitle(
                            "Scheduled Maintenance - "
                                    + machine.getName()
                    );

                    task.setDescription(
                            "Machine has reached "
                                    + daysPassed
                                    + " days since last maintenance."
                    );

                    task.setStatus("OPEN");
                    task.setMachine(machine);

                    maintenanceTaskRepository.save(task);
                }
            }
        }
    }
}