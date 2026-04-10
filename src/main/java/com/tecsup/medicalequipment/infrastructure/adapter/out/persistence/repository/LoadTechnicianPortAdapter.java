package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.application.port.out.LoadTechnicianPort;
import com.tecsup.medicalequipment.domain.model.Technician;
import com.tecsup.medicalequipment.domain.repository.TechnicianRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador que implementa LoadTechnicianPort (puerto de salida de aplicación).
 * Proporciona a la capa de aplicación la capacidad de cargar técnicos.
 */
@Component
public class LoadTechnicianPortAdapter implements LoadTechnicianPort {

    private final TechnicianRepository technicianRepository;

    public LoadTechnicianPortAdapter(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    @Override
    public Optional<Technician> loadTechnicianById(String technicianId) {
        if (technicianId == null || technicianId.isBlank()) {
            return Optional.empty();
        }

        try {
            return technicianRepository.findById(technicianId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
