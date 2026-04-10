package com.tecsup.medicalequipment.infrastructure.adapter.in.web.dto;

/**
 * DTO de Response para programar un mantenimiento.
 * Retorna resultado al cliente HTTP.
 */
public class ScheduleMaintenanceApiResponse {

    private String scheduleId;
    private String equipmentId;
    private String status;
    private String message;

    // Constructor vacío para Jackson
    public ScheduleMaintenanceApiResponse() {
    }

    public ScheduleMaintenanceApiResponse(String scheduleId, String equipmentId,
                                         String status, String message) {
        this.scheduleId = scheduleId;
        this.equipmentId = equipmentId;
        this.status = status;
        this.message = message;
    }

    public static ScheduleMaintenanceApiResponse success(String scheduleId, String equipmentId) {
        return new ScheduleMaintenanceApiResponse(
            scheduleId,
            equipmentId,
            "SUCCESS",
            "Mantenimiento programado exitosamente"
        );
    }

    public static ScheduleMaintenanceApiResponse error(String message) {
        return new ScheduleMaintenanceApiResponse(
            null,
            null,
            "ERROR",
            message
        );
    }

    // Getters y Setters
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
