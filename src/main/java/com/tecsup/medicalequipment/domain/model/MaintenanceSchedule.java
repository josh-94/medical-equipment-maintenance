package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.exception.InvalidMaintenanceScheduleDateException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa un Cronograma de Mantenimiento programado.
 * Enforza la regla de negocio: Un mantenimiento no puede programarse en fecha pasada.
 */
public class MaintenanceSchedule {

    private final String id;
    private final String equipmentId;
    private final LocalDateTime scheduledDate;
    private final MaintenanceType type;
    private final String technicianId;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;
    private boolean completed;

    private MaintenanceSchedule(String id, String equipmentId, LocalDateTime scheduledDate,
                                MaintenanceType type, String technicianId) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.scheduledDate = scheduledDate;
        this.type = type;
        this.technicianId = technicianId;
        this.completed = false;
    }

    /**
     * Factory method para crear un nuevo cronograma de mantenimiento.
     * REGLA: No puede estar en fecha pasada.
     */
    public static MaintenanceSchedule create(String equipmentId, LocalDateTime scheduledDate,
                                            MaintenanceType type, String technicianId) {
        validateSchedule(equipmentId, scheduledDate, type, technicianId);
        
        if (scheduledDate.isBefore(LocalDateTime.now())) {
            throw new InvalidMaintenanceScheduleDateException(
                "No se puede programar un mantenimiento en una fecha pasada: " + scheduledDate
            );
        }

        return new MaintenanceSchedule(UUID.randomUUID().toString(), equipmentId, scheduledDate, type, technicianId);
    }

    /**
     * Reconstruir una entidad existente desde persistencia.
     * Se permite cualquier fecha al reconstruir desde BD.
     */
    public static MaintenanceSchedule reconstruct(String id, String equipmentId, LocalDateTime scheduledDate,
                                                  MaintenanceType type, String technicianId,
                                                  LocalDateTime actualStartDate, LocalDateTime actualEndDate,
                                                  boolean completed) {
        validateSchedule(equipmentId, scheduledDate, type, technicianId);
        
        MaintenanceSchedule schedule = new MaintenanceSchedule(id, equipmentId, scheduledDate, type, technicianId);
        schedule.actualStartDate = actualStartDate;
        schedule.actualEndDate = actualEndDate;
        schedule.completed = completed;
        return schedule;
    }

    private static void validateSchedule(String equipmentId, LocalDateTime scheduledDate,
                                        MaintenanceType type, String technicianId) {
        if (equipmentId == null || equipmentId.isBlank()) {
            throw new DomainException("El ID del equipo no puede estar vacío");
        }
        if (scheduledDate == null) {
            throw new DomainException("La fecha programada no puede ser nula");
        }
        if (type == null) {
            throw new DomainException("El tipo de mantenimiento no puede ser nulo");
        }
        if (technicianId == null || technicianId.isBlank()) {
            throw new DomainException("El ID del técnico no puede estar vacío");
        }
    }

    /**
     * Marcar el inicio del mantenimiento.
     */
    public void startMaintenance() {
        if (completed) {
            throw new DomainException("No se puede reiniciar un mantenimiento ya completado");
        }
        this.actualStartDate = LocalDateTime.now();
    }

    /**
     * Marcar el final del mantenimiento.
     */
    public void completeMaintenance() {
        if (completed) {
            throw new DomainException("El mantenimiento ya ha sido completado");
        }
        if (actualStartDate == null) {
            throw new DomainException("Debe iniciarse el mantenimiento antes de completarlo");
        }
        this.actualEndDate = LocalDateTime.now();
        this.completed = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public MaintenanceType getType() {
        return type;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public LocalDateTime getActualStartDate() {
        return actualStartDate;
    }

    public LocalDateTime getActualEndDate() {
        return actualEndDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceSchedule that = (MaintenanceSchedule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MaintenanceSchedule{" +
                "id='" + id + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", type=" + type +
                ", completed=" + completed +
                '}';
    }
}
