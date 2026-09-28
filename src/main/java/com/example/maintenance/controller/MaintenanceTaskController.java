package com.example.maintenance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maintenance.model.MaintenanceTask;
import com.example.maintenance.service.MaintenanceTaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class MaintenanceTaskController {

    private final MaintenanceTaskService maintenanceTaskService;

    public MaintenanceTaskController(
            MaintenanceTaskService maintenanceTaskService) {
        this.maintenanceTaskService = maintenanceTaskService;
    }

    @GetMapping
    public List<MaintenanceTask> getAllTasks() {
        return maintenanceTaskService.getAllTasks();
    }

    @GetMapping("/overdue")
    public List<MaintenanceTask> getOverdueTasks() {
        return maintenanceTaskService.getOverdueTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTask> getTaskById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                maintenanceTaskService.getTaskById(id)
        );
    }

    @PostMapping
    public ResponseEntity<MaintenanceTask> createTask(
            @Valid @RequestBody MaintenanceTask task) {

        MaintenanceTask createdTask =
                maintenanceTaskService.createTask(task);

        return new ResponseEntity<>(
                createdTask,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTask> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceTask task) {

        return ResponseEntity.ok(
                maintenanceTaskService.updateTask(id, task)
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<MaintenanceTask> completeTask(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Long technicianId =
                Long.valueOf(request.get("technicianId").toString());

        String notes =
                request.get("notes") != null
                        ? request.get("notes").toString()
                        : "";

        return ResponseEntity.ok(
                maintenanceTaskService.completeTask(
                        id,
                        technicianId,
                        notes
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        maintenanceTaskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}