package com.example.maintenance.controller;

import java.util.List;

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

import com.example.maintenance.model.UsageLog;
import com.example.maintenance.service.UsageLogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usage-logs")
public class UsageLogController {

    private final UsageLogService usageLogService;

    public UsageLogController(UsageLogService usageLogService) {
        this.usageLogService = usageLogService;
    }

    @GetMapping
    public List<UsageLog> getAllUsageLogs() {
        return usageLogService.getAllUsageLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsageLog> getUsageLogById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                usageLogService.getUsageLogById(id)
        );
    }

    @PostMapping
    public ResponseEntity<UsageLog> createUsageLog(
            @Valid @RequestBody UsageLog usageLog) {

        UsageLog createdUsageLog =
                usageLogService.createUsageLog(usageLog);

        return new ResponseEntity<>(
                createdUsageLog,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsageLog> updateUsageLog(
            @PathVariable Long id,
            @Valid @RequestBody UsageLog usageLog) {

        return ResponseEntity.ok(
                usageLogService.updateUsageLog(
                        id,
                        usageLog
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsageLog(
            @PathVariable Long id) {

        usageLogService.deleteUsageLog(id);

        return ResponseEntity.noContent().build();
    }
}