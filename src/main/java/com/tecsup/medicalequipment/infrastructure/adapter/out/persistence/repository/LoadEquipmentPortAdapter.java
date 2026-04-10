package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.application.port.out.LoadEquipmentPort;
import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.domain.repository.EquipmentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador que implementa LoadEquipmentPort (puerto de salida de aplicación).
 * Proporciona a la capa de aplicación la capacidad de cargar equipos.
 */
@Component
public class LoadEquipmentPortAdapter implements LoadEquipmentPort {

    private final EquipmentRepository equipmentRepository;

    public LoadEquipmentPortAdapter(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Optional<Equipment> loadEquipmentById(String equipmentId) {
        if (equipmentId == null || equipmentId.isBlank()) {
            return Optional.empty();
        }

        try {
            return equipmentRepository.findById(EquipmentId.create(equipmentId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
