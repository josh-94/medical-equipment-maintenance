package com.tecsup.medicalequipment.domain.service;

import com.tecsup.medicalequipment.domain.exception.ConflictingScheduleException;
import com.tecsup.medicalequipment.domain.exception.InvalidMaintenanceVisitException;
import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.model.MaintenanceVisit;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import com.tecsup.medicalequipment.domain.repository.MaintenanceVisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio de dominio que implementa reglas de negocio complejas para MaintenanceVisit.
 * REGLA: Una visita solo puede registrarse sobre un mantenimiento programado existente.
 * REGLA: No puede haber dos visitas del mismo técnico en el mismo horario.
 */
@Service
public class MaintenanceVisitService {

    private final MaintenanceVisitRepository maintenanceVisitRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;

    public MaintenanceVisitService(MaintenanceVisitRepository maintenanceVisitRepository,
                                  MaintenanceScheduleRepository maintenanceScheduleRepository) {
        this.maintenanceVisitRepository = maintenanceVisitRepository;
        this.maintenanceScheduleRepository = maintenanceScheduleRepository;
    }

    /**
     * Registra una visita de mantenimiento con validaciones de negocio.
     * REGLA: Una visita solo puede registrarse sobre un mantenimiento programado existente.
     * REGLA: No puede haber dos visitas del mismo técnico en el mismo horario.
     *
     * @param visit la visita a registrar
     * @throws InvalidMaintenanceVisitException si el cronograma no existe o la visita es inválida
     * @throws ConflictingScheduleException si hay conflicto de horarios
     */
    public void registerVisit(MaintenanceVisit visit) {
        // Validar que existe el cronograma de mantenimiento
        Optional<MaintenanceSchedule> scheduledOptional = 
            maintenanceScheduleRepository.findById(visit.getMaintenanceScheduleId());

        if (scheduledOptional.isEmpty()) {
            throw new InvalidMaintenanceVisitException(
                "No existe cronograma de mantenimiento con ID: " + visit.getMaintenanceScheduleId()
            );
        }

        MaintenanceSchedule schedule = scheduledOptional.get();

        // Validar que no hay conflicto de horarios para el técnico
        validateNoConflictForTechnician(visit.getTechnicianId(), visit.getVisitDate());

        // Guardar la visita
        maintenanceVisitRepository.save(visit);
    }

    /**
     * Valida que no haya conflicto de horarios para el técnico en la fecha especificada.
     * REGLA: No puede haber dos visitas del mismo técnico en el mismo horario.
     *
     * @param technicianId ID del técnico
     * @param visitDate fecha y hora de la visita
     * @throws ConflictingScheduleException si hay conflicto
     */
    private void validateNoConflictForTechnician(String technicianId, LocalDateTime visitDate) {
        // Verificar si hay otras visitas para el técnico en la misma fecha
        if (maintenanceVisitRepository.countVisitsForTechnicianAtDateTime(technicianId, visitDate) > 0) {
            throw new ConflictingScheduleException(
                "El técnico " + technicianId + " ya tiene una visita en " + visitDate
            );
        }

        // Verificar si hay cronogramas programados para el técnico en la misma fecha
        if (maintenanceScheduleRepository.existsForTechnicianAtDateTime(technicianId, visitDate)) {
            throw new ConflictingScheduleException(
                "El técnico " + technicianId + " ya tiene un cronograma en " + visitDate
            );
        }
    }
}
