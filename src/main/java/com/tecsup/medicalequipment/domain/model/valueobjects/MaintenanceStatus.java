package com.tecsup.medicalequipment.domain.model.valueobjects;

/**
 * Value Object que representa el estado del equipo desde el punto de vista del mantenimiento.
 */
public enum MaintenanceStatus {

    OPERATIONAL("Operacional"),
    UNDER_MAINTENANCE("En mantenimiento"),
    OUT_OF_SERVICE("Fuera de servicio"),
    NEEDS_MAINTENANCE("Requiere mantenimiento");

    private final String description;

    MaintenanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
