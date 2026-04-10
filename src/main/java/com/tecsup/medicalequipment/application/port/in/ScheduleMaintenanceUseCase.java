package com.tecsup.medicalequipment.application.port.in;

import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceCommand;
import com.tecsup.medicalequipment.application.dto.ScheduleMaintenanceResponse;

/**
 * Puerto de entrada (Use Case) para el caso de uso: Programar un Mantenimiento.
 * Define el contrato de entrada del casos de uso.
 * Sin dependencias de Spring ni frameworks.
 */
public interface ScheduleMaintenanceUseCase {

    /**
     * Ejecuta el caso de uso de programación de mantenimiento.
     *
     * @param command comando con los datos necesarios para programar el mantenimiento
     * @return respuesta con el ID del cronograma creado
     * @throws IllegalArgumentException si los datos del comando son inválidos
     * @throws com.tecsup.medicalequipment.domain.exception.DomainException si se viola una regla de negocio
     */
    ScheduleMaintenanceResponse execute(ScheduleMaintenanceCommand command);
}
