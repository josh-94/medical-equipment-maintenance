package com.tecsup.medicalequipment.application.dto;

import java.time.LocalDateTime;

/**
 * Command DTO para el caso de uso: Programar un Mantenimiento.
 * Contiene los datos de entrada desde la interfaz de usuario o API.
 */
public class ScheduleMaintenanceCommand {

    private final String equipmentId;
    private final LocalDateTime scheduledDate;
    private final String maintenanceType;
    private final String technicianId;

    private ScheduleMaintenanceCommand(String equipmentId, LocalDateTime scheduledDate,
                                       String maintenanceType, String technicianId) {
        this.equipmentId = equipmentId;
        this.scheduledDate = scheduledDate;
        this.maintenanceType = maintenanceType;
        this.technicianId = technicianId;
    }

    /**
     * Factory method para crear un comando de programación de mantenimiento.
     */
    public static ScheduleMaintenanceCommand create(String equipmentId, LocalDateTime scheduledDate,
                                                    String maintenanceType, String technicianId) {
        if (equipmentId == null || equipmentId.isBlank()) {
            throw new IllegalArgumentException("El ID del equipo no puede estar vacío");
        }
        if (scheduledDate == null) {
            throw new IllegalArgumentException("La fecha programada no puede ser nula");
        }
        if (maintenanceType == null || maintenanceType.isBlank()) {
            throw new IllegalArgumentException("El tipo de mantenimiento no puede estar vacío");
        }
        if (technicianId == null || technicianId.isBlank()) {
            throw new IllegalArgumentException("El ID del técnico no puede estar vacío");
        }

        return new ScheduleMaintenanceCommand(equipmentId, scheduledDate, maintenanceType, technicianId);
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public String getTechnicianId() {
        return technicianId;
    }
}
