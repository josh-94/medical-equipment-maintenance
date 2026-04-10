package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

/**
 * Enumeración JPA que mapea MaintenanceType del dominio.
 */
public enum MaintenanceTypeJpaEnum {
    PREVENTIVE,
    CORRECTIVE,
    INSPECTION,
    INSTALLATION,
    DECOMMISSIONING
}
