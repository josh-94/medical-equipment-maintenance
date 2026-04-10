package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

/**
 * Enumeración JPA que mapea MaintenanceStatus del dominio.
 * Se usa en la base de datos para representar el estado del equipo.
 */
public enum MaintenanceStatusJpaEnum {
    OPERATIONAL,
    UNDER_MAINTENANCE,
    OUT_OF_SERVICE,
    NEEDS_MAINTENANCE
}
