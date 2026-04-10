package com.tecsup.medicalequipment.application.port.out;

import com.tecsup.medicalequipment.domain.model.Equipment;

import java.util.Optional;

/**
 * Puerto de salida que define lo que el caso de uso necesita del exterior.
 * Proporciona acceso a datos de Equipment sin exponer las implementaciones específicas.
 */
public interface LoadEquipmentPort {

    /**
     * Carga un equipo por su ID.
     *
     * @param equipmentId el ID del equipo a cargar
     * @return un Optional conteniendo el equipo si existe
     */
    Optional<Equipment> loadEquipmentById(String equipmentId);
}
