package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.application.port.out.SaveMaintenanceSchedulePort;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import org.springframework.stereotype.Component;

/**
 * Adaptador que implementa SaveMaintenanceSchedulePort (puerto de salida de aplicación).
 * Proporciona a la capa de aplicación la capacidad de guardar cronogramas de mantenimiento.
 */
@Component
public class SaveMaintenanceSchedulePortAdapter implements SaveMaintenanceSchedulePort {

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;

    public SaveMaintenanceSchedulePortAdapter(MaintenanceScheduleRepository maintenanceScheduleRepository) {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
    }

    @Override
    public void saveSchedule(MaintenanceSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("El cronograma no puede ser nulo");
        }

        maintenanceScheduleRepository.save(schedule);
    }
}
