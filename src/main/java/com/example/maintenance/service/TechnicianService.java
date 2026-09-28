package com.example.maintenance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.maintenance.model.Technician;
import com.example.maintenance.repository.TechnicianRepository;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    public Technician getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Technician not found with id: " + id));
    }

    public Technician createTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    public Technician updateTechnician(
            Long id,
            Technician updatedTechnician) {

        Technician existingTechnician = getTechnicianById(id);

        existingTechnician.setName(updatedTechnician.getName());
        existingTechnician.setEmail(updatedTechnician.getEmail());
        existingTechnician.setSkill(updatedTechnician.getSkill());

        return technicianRepository.save(existingTechnician);
    }

    public void deleteTechnician(Long id) {
        Technician technician = getTechnicianById(id);
        technicianRepository.delete(technician);
    }
}