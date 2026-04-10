package com.tecsup.medicalequipment.application.port.out;

import com.tecsup.medicalequipment.domain.model.Technician;

import java.util.Optional;

/**
 * Puerto de salida que define lo que el caso de uso necesita para acceder a técnicos.
 * Proporciona acceso a datos de Technician sin exponer las implementaciones específicas.
 */
public interface LoadTechnicianPort {

    /**
     * Carga un técnico por su ID.
     *
     * @param technicianId el ID del técnico a cargar
     * @return un Optional conteniendo el técnico si existe
     */
    Optional<Technician> loadTechnicianById(String technicianId);
}
