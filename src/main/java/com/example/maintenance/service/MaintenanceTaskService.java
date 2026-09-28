package com.example.maintenance.service;

import com.example.maintenance.model.MaintenanceTask;
import com.example.maintenance.model.Machine;
import com.example.maintenance.model.Technician;
import com.example.maintenance.repository.MaintenanceTaskRepository;
import com.example.maintenance.repository.MachineRepository;
import com.example.maintenance.repository.TechnicianRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceTaskService {

    private final MaintenanceTaskRepository maintenanceTaskRepository;
    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;

    public MaintenanceTaskService(
            MaintenanceTaskRepository maintenanceTaskRepository,
            MachineRepository machineRepository,
            TechnicianRepository technicianRepository) {

        this.maintenanceTaskRepository = maintenanceTaskRepository;
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
    }

    public List<MaintenanceTask> getAllTasks() {
        return maintenanceTaskRepository.findAll();
    }

    public MaintenanceTask getTaskById(Long id) {
        return maintenanceTaskRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Maintenance task not found with id: " + id));
    }

    public MaintenanceTask createTask(MaintenanceTask task) {

        if (task.getMachine() == null || task.getMachine().getId() == null) {
            throw new RuntimeException("Machine is required");
        }

        Machine machine = machineRepository.findById(task.getMachine().getId())
                .orElseThrow(() ->
                        new RuntimeException("Machine not found"));

        boolean openTask = maintenanceTaskRepository
                .existsByMachineIdAndStatusNot(machine.getId(), "COMPLETED");

        if (openTask) {
            throw new RuntimeException(
                    "An open maintenance task already exists for this machine");
        }

        task.setMachine(machine);

        if (task.getStatus() == null || task.getStatus().isBlank()) {
            task.setStatus("OPEN");
        }

        return maintenanceTaskRepository.save(task);
    }

    public MaintenanceTask updateTask(Long id, MaintenanceTask updatedTask) {

        MaintenanceTask existingTask = getTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());

        if (updatedTask.getTechnician() != null
                && updatedTask.getTechnician().getId() != null) {

            Technician technician = technicianRepository
                    .findById(updatedTask.getTechnician().getId())
                    .orElseThrow(() ->
                            new RuntimeException("Technician not found"));

            existingTask.setTechnician(technician);
        }

        return maintenanceTaskRepository.save(existingTask);
    }

    public MaintenanceTask completeTask(
            Long id,
            Long technicianId,
            String notes) {

        MaintenanceTask task = getTaskById(id);

        if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {
            throw new RuntimeException("Task is already completed");
        }

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() ->
                        new RuntimeException("Technician not found"));

        task.setTechnician(technician);
        task.setNotes(notes);
        task.setStatus("COMPLETED");
        task.setCompletedDate(LocalDate.now());

        Machine machine = task.getMachine();

        machine.setCurrentUsageHours(0);
        machine.setLastMaintenanceDate(LocalDate.now());

        machineRepository.save(machine);

        return maintenanceTaskRepository.save(task);
    }

    public List<MaintenanceTask> getOverdueTasks() {
        return maintenanceTaskRepository
                .findByDueDateBeforeAndStatusNot(LocalDate.now(), "COMPLETED");
    }

    public void deleteTask(Long id) {
        MaintenanceTask task = getTaskById(id);
        maintenanceTaskRepository.delete(task);
    }
}