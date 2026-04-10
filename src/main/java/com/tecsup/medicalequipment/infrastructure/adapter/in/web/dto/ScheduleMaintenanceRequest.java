package com.tecsup.medicalequipment.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;

/**
 * DTO de Request para programar un mantenimiento.
 * Recibe datos del cliente HTTP.
 */
public class ScheduleMaintenanceRequest {

    private String equipmentId;
    private LocalDateTime scheduledDate;
    private String maintenanceType;
    private String technicianId;

    // Constructor vacío para Jackson
    public ScheduleMaintenanceRequest() {
    }

    public ScheduleMaintenanceRequest(String equipmentId, LocalDateTime scheduledDate,
                                     String maintenanceType, String technicianId) {
        this.equipmentId = equipmentId;
        this.scheduledDate = scheduledDate;
        this.maintenanceType = maintenanceType;
        this.technicianId = technicianId;
    }

    // Getters y Setters
    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }
}
