package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.exception.InvalidMaintenanceVisitException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa una Visita de Mantenimiento registrada.
 * REGLA: Solo puede registrarse sobre un mantenimiento programado existente.
 */
public class MaintenanceVisit {

    private final String id;
    private final String maintenanceScheduleId;
    private final LocalDateTime visitDate;
    private final String observations;
    private final String technicianId;

    private MaintenanceVisit(String id, String maintenanceScheduleId, LocalDateTime visitDate,
                            String observations, String technicianId) {
        this.id = id;
        this.maintenanceScheduleId = maintenanceScheduleId;
        this.visitDate = visitDate;
        this.observations = observations;
        this.technicianId = technicianId;
    }

    /**
     * Factory method para crear una nueva visita de mantenimiento.
     * REGLA: Una visita solo puede registrarse sobre un mantenimiento programado existente.
     * Por lo tanto, se recibe la MaintenanceSchedule completa para validarla.
     */
    public static MaintenanceVisit create(MaintenanceSchedule schedule, LocalDateTime visitDate,
                                         String observations, String technicianId) {
        validateVisit(schedule, visitDate, observations, technicianId);

        // REGLA: La visita solo puede registrarse si hay un mantenimiento programado existente
        if (schedule == null) {
            throw new InvalidMaintenanceVisitException(
                "No hay mantenimiento programado. No se puede registrar una visita sin schedule."
            );
        }

        return new MaintenanceVisit(UUID.randomUUID().toString(), schedule.getId(), visitDate,
                                   observations != null ? observations : "", technicianId);
    }

    /**
     * Reconstruir una entidad existente desde persistencia.
     */
    public static MaintenanceVisit reconstruct(String id, String maintenanceScheduleId,
                                              LocalDateTime visitDate, String observations,
                                              String technicianId) {
        validateVisitFields(maintenanceScheduleId, visitDate, technicianId);
        return new MaintenanceVisit(id, maintenanceScheduleId, visitDate,
                                   observations != null ? observations : "", technicianId);
    }

    private static void validateVisit(MaintenanceSchedule schedule, LocalDateTime visitDate,
                                     String observations, String technicianId) {
        if (schedule == null) {
            throw new InvalidMaintenanceVisitException(
                "El cronograma de mantenimiento no puede ser nulo"
            );
        }
        validateVisitFields(schedule.getId(), visitDate, technicianId);
    }

    private static void validateVisitFields(String scheduleId, LocalDateTime visitDate, String technicianId) {
        if (scheduleId == null || scheduleId.isBlank()) {
            throw new DomainException("El ID del cronograma no puede estar vacío");
        }
        if (visitDate == null) {
            throw new DomainException("La fecha de visita no puede ser nula");
        }
        if (technicianId == null || technicianId.isBlank()) {
            throw new DomainException("El ID del técnico no puede estar vacío");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getMaintenanceScheduleId() {
        return maintenanceScheduleId;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public String getObservations() {
        return observations;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceVisit that = (MaintenanceVisit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MaintenanceVisit{" +
                "id='" + id + '\'' +
                ", maintenanceScheduleId='" + maintenanceScheduleId + '\'' +
                ", visitDate=" + visitDate +
                ", technicianId='" + technicianId + '\'' +
                '}';
    }
}
