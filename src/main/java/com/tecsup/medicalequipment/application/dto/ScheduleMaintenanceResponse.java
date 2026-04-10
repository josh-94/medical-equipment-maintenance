package com.tecsup.medicalequipment.application.dto;

/**
 * Response DTO para el caso de uso: Programar un Mantenimiento.
 * Contiene los datos de salida del caso de uso.
 */
public class ScheduleMaintenanceResponse {

    private final String scheduleId;
    private final String equipmentId;
    private final String statusMessage;

    private ScheduleMaintenanceResponse(String scheduleId, String equipmentId, String statusMessage) {
        this.scheduleId = scheduleId;
        this.equipmentId = equipmentId;
        this.statusMessage = statusMessage;
    }

    /**
     * Factory method para crear una respuesta exitosa.
     */
    public static ScheduleMaintenanceResponse success(String scheduleId, String equipmentId) {
        return new ScheduleMaintenanceResponse(
            scheduleId,
            equipmentId,
            "Mantenimiento programado exitosamente"
        );
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
