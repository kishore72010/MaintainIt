package com.example.maintenance.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Machine name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Serial number is required")
    @Column(nullable = false, unique = true)
    private String serialNumber;

    @NotBlank(message = "Machine status is required")
    @Column(nullable = false)
    private String status;

    private String location;

    @Column(nullable = false)
    private double maintenanceInterval;

    @Column(nullable = false)
    private String intervalType;

    @Column(nullable = false)
    private double currentUsageHours = 0;

    private LocalDate lastMaintenanceDate;

    @JsonIgnore
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaintenanceTask> maintenanceTasks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsageLog> usageLogs = new ArrayList<>();

    public Machine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getMaintenanceInterval() {
        return maintenanceInterval;
    }

    public void setMaintenanceInterval(double maintenanceInterval) {
        this.maintenanceInterval = maintenanceInterval;
    }

    public String getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(String intervalType) {
        this.intervalType = intervalType;
    }

    public double getCurrentUsageHours() {
        return currentUsageHours;
    }

    public void setCurrentUsageHours(double currentUsageHours) {
        this.currentUsageHours = currentUsageHours;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public List<MaintenanceTask> getMaintenanceTasks() {
        return maintenanceTasks;
    }

    public void setMaintenanceTasks(List<MaintenanceTask> maintenanceTasks) {
        this.maintenanceTasks = maintenanceTasks;
    }

    public List<UsageLog> getUsageLogs() {
        return usageLogs;
    }

    public void setUsageLogs(List<UsageLog> usageLogs) {
        this.usageLogs = usageLogs;
    }
}