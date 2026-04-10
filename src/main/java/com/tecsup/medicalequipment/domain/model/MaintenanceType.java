package com.tecsup.medicalequipment.domain.model;

/**
 * Enumeración que representa los tipos de mantenimiento disponibles.
 */
public enum MaintenanceType {

    PREVENTIVE("Preventivo"),
    CORRECTIVE("Correctivo"),
    INSPECTION("Inspección"),
    INSTALLATION("Instalación"),
    DECOMMISSIONING("Decomisión");

    private final String description;

    MaintenanceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
