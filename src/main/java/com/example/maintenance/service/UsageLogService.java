package com.example.maintenance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.maintenance.model.Machine;
import com.example.maintenance.model.MaintenanceTask;
import com.example.maintenance.model.UsageLog;
import com.example.maintenance.repository.MachineRepository;
import com.example.maintenance.repository.MaintenanceTaskRepository;
import com.example.maintenance.repository.UsageLogRepository;

@Service
public class UsageLogService {

    private final UsageLogRepository usageLogRepository;
    private final MachineRepository machineRepository;
    private final MaintenanceTaskRepository maintenanceTaskRepository;

    public UsageLogService(
            UsageLogRepository usageLogRepository,
            MachineRepository machineRepository,
            MaintenanceTaskRepository maintenanceTaskRepository) {

        this.usageLogRepository = usageLogRepository;
        this.machineRepository = machineRepository;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
    }

    public List<UsageLog> getAllUsageLogs() {
        return usageLogRepository.findAll();
    }

    public UsageLog getUsageLogById(Long id) {
        return usageLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usage log not found with id: " + id));
    }

    public UsageLog createUsageLog(UsageLog usageLog) {

        if (usageLog.getMachine() == null
                || usageLog.getMachine().getId() == null) {
            throw new RuntimeException("Machine is required");
        }

        if (usageLog.getHoursUsed() <= 0) {
            throw new RuntimeException("Usage hours must be greater than 0");
        }

        Machine machine = machineRepository
                .findById(usageLog.getMachine().getId())
                .orElseThrow(() ->
                        new RuntimeException("Machine not found"));

        usageLog.setMachine(machine);

        if (usageLog.getTimestamp() == null) {
            usageLog.setTimestamp(LocalDateTime.now());
        }

        machine.setCurrentUsageHours(
                machine.getCurrentUsageHours()
                        + usageLog.getHoursUsed()
        );

        machineRepository.save(machine);

        UsageLog savedLog = usageLogRepository.save(usageLog);

        checkMaintenanceRequired(machine);

        return savedLog;
    }

    private void checkMaintenanceRequired(Machine machine) {

        if (!"HOURS".equalsIgnoreCase(machine.getIntervalType())) {
            return;
        }

        double usage = machine.getCurrentUsageHours();
        double interval = machine.getMaintenanceInterval();

        if (interval <= 0) {
            return;
        }

        double percentage = usage / interval;

        if (percentage >= 0.80) {

            boolean openTask =
                    maintenanceTaskRepository
                            .existsByMachineIdAndStatusNot(
                                    machine.getId(),
                                    "COMPLETED"
                            );

            if (!openTask) {

                MaintenanceTask task = new MaintenanceTask();

                task.setTitle(
                        "Preventive Maintenance - "
                                + machine.getName()
                );

                task.setDescription(
                        "Machine has reached "
                                + (int) (percentage * 100)
                                + "% of its maintenance interval."
                );

                task.setStatus("OPEN");
                task.setMachine(machine);

                maintenanceTaskRepository.save(task);
            }
        }
    }

    public UsageLog updateUsageLog(
            Long id,
            UsageLog updatedUsageLog) {

        UsageLog existingLog = getUsageLogById(id);

        existingLog.setTimestamp(
                updatedUsageLog.getTimestamp()
        );

        existingLog.setHoursUsed(
                updatedUsageLog.getHoursUsed()
        );

        return usageLogRepository.save(existingLog);
    }

    public void deleteUsageLog(Long id) {

        UsageLog usageLog = getUsageLogById(id);

        usageLogRepository.delete(usageLog);
    }
}