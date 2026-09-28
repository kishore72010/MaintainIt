package com.example.maintenance.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.maintenance.model.Machine;
import com.example.maintenance.repository.MachineRepository;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    public Machine getMachineById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Machine not found with id: " + id));
    }

    public Machine createMachine(Machine machine) {

        if (machine.getLastMaintenanceDate() == null) {
            machine.setLastMaintenanceDate(LocalDate.now());
        }

        if (machine.getCurrentUsageHours() < 0) {
            machine.setCurrentUsageHours(0);
        }

        return machineRepository.save(machine);
    }

    public Machine updateMachine(
            Long id,
            Machine updatedMachine) {

        Machine existingMachine = getMachineById(id);

        existingMachine.setName(updatedMachine.getName());
        existingMachine.setSerialNumber(
                updatedMachine.getSerialNumber());
        existingMachine.setStatus(updatedMachine.getStatus());
        existingMachine.setLocation(updatedMachine.getLocation());
        existingMachine.setMaintenanceInterval(
                updatedMachine.getMaintenanceInterval());
        existingMachine.setIntervalType(
                updatedMachine.getIntervalType());

        return machineRepository.save(existingMachine);
    }

    public void deleteMachine(Long id) {
        Machine machine = getMachineById(id);
        machineRepository.delete(machine);
    }

    public List<Machine> getOverdueMachines() {

        List<Machine> machines = machineRepository.findAll();

        List<Machine> overdueMachines = new ArrayList<>();

        for (Machine machine : machines) {

            if ("HOURS".equalsIgnoreCase(
                    machine.getIntervalType())) {

                if (machine.getCurrentUsageHours()
                        >= machine.getMaintenanceInterval()) {

                    overdueMachines.add(machine);
                }
            }

            if ("DAYS".equalsIgnoreCase(
                    machine.getIntervalType())) {

                if (machine.getLastMaintenanceDate() != null) {

                    long days = ChronoUnit.DAYS.between(
                            machine.getLastMaintenanceDate(),
                            LocalDate.now()
                    );

                    if (days >= machine.getMaintenanceInterval()) {
                        overdueMachines.add(machine);
                    }
                }
            }
        }

        return overdueMachines;
    }
}