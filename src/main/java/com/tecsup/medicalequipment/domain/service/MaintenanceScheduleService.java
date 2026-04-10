package com.tecsup.medicalequipment.domain.service;

import com.tecsup.medicalequipment.domain.exception.ConflictingScheduleException;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import com.tecsup.medicalequipment.domain.repository.MaintenanceVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio de dominio que implementa reglas de negocio complejas para MaintenanceSchedule.
 * REGLA: No puede haber dos visitas del mismo técnico en el mismo horario.
 */
@Service
public class MaintenanceScheduleService {

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final MaintenanceVisitRepository maintenanceVisitRepository;

    public MaintenanceScheduleService(MaintenanceScheduleRepository maintenanceScheduleRepository,
                                     MaintenanceVisitRepository maintenanceVisitRepository) {
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
        this.maintenanceVisitRepository = maintenanceVisitRepository;
    }

    /**
     * Valida que no haya conflicto de horarios para el técnico.
     * REGLA: No puede haber dos visitas del mismo técnico en el mismo horario.
     *
     * @param schedule el cronograma a validar
     * @throws ConflictingScheduleException si hay conflicto de horarios
     */
    public void validateScheduleForTechnician(MaintenanceSchedule schedule) {
        String technicianId = schedule.getTechnicianId();
        LocalDateTime scheduledDate = schedule.getScheduledDate();

        // Verificar si hay otros cronogramas para el técnico en la misma fecha
        if (maintenanceScheduleRepository.existsForTechnicianAtDateTime(technicianId, scheduledDate)) {
            throw new ConflictingScheduleException(
                "El técnico " + technicianId + " ya tiene un cronograma en " + scheduledDate
            );
        }

        // Verificar si hay visitas registradas para el técnico en la misma fecha
        if (maintenanceVisitRepository.countVisitsForTechnicianAtDateTime(technicianId, scheduledDate) > 0) {
            throw new ConflictingScheduleException(
                "El técnico " + technicianId + " ya tiene una visita registrada en " + scheduledDate
            );
        }
    }

    /**
     * Registra un cronograma de mantenimiento después de validar conflictos.
     *
     * @param schedule el cronograma a registrar
     */
    public void registerSchedule(MaintenanceSchedule schedule) {
        validateScheduleForTechnician(schedule);
        maintenanceScheduleRepository.save(schedule);
    }
}
