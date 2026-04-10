package com.tecsup.medicalequipment.application.port.out;

import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;

/**
 * Puerto de salida que define cómo persistir cronogramas de mantenimiento.
 * Abstrae la implementación específica de la persistencia.
 */
public interface SaveMaintenanceSchedulePort {

    /**
     * Guarda un cronograma de mantenimiento.
     *
     * @param schedule el cronograma a guardar
     */
    void saveSchedule(MaintenanceSchedule schedule);
}
