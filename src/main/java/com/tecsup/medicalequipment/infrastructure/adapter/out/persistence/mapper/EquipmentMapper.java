package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper;

import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.domain.model.valueobjects.MaintenanceStatus;
import com.tecsup.medicalequipment.domain.model.valueobjects.SerialNumber;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.EquipmentJpaEntity;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceStatusJpaEnum;

/**
 * Mapper para convertir entre Equipment (dominio) y EquipmentJpaEntity (persistencia).
 * Responsable de adaptar la capa de persistencia con la capa de dominio.
 * El dominio no sabe nada de JPA.
 */
public class EquipmentMapper {

    /**
     * Convierte una entidad JPA a una entidad de dominio.
     * No se lanzan excepciones de validación aquí porque la entidad ya está persistida.
     */
    public static Equipment toDomain(EquipmentJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return Equipment.reconstruct(
            EquipmentId.create(jpaEntity.getId()),
            jpaEntity.getName(),
            jpaEntity.getBrand(),
            jpaEntity.getModel(),
            SerialNumber.create(jpaEntity.getSerialNumber()),
            jpaEntity.getWarrantyEndDate(),
            mapJpaStatusToDomainStatus(jpaEntity.getStatus()),
            jpaEntity.getInstallationDate()
        );
    }

    /**
     * Convierte una entidad de dominio a una entidad JPA.
     * Extrae los valores de los Value Objects y los prepara para persistencia.
     */
    public static EquipmentJpaEntity toJpaEntity(Equipment equipment) {
        if (equipment == null) {
            return null;
        }

        return new EquipmentJpaEntity(
            equipment.getId().getValue(),
            equipment.getName(),
            equipment.getBrand(),
            equipment.getModel(),
            equipment.getSerialNumber().getValue(),
            equipment.getWarrantyEndDate(),
            equipment.getInstallationDate(),
            mapDomainStatusToJpaStatus(equipment.getStatus())
        );
    }

    /**
     * Mapea del enum JPA al enum del dominio.
     */
    private static MaintenanceStatus mapJpaStatusToDomainStatus(MaintenanceStatusJpaEnum jpaStatus) {
        if (jpaStatus == null) {
            return MaintenanceStatus.OPERATIONAL;
        }

        return switch (jpaStatus) {
            case OPERATIONAL -> MaintenanceStatus.OPERATIONAL;
            case UNDER_MAINTENANCE -> MaintenanceStatus.UNDER_MAINTENANCE;
            case OUT_OF_SERVICE -> MaintenanceStatus.OUT_OF_SERVICE;
            case NEEDS_MAINTENANCE -> MaintenanceStatus.NEEDS_MAINTENANCE;
        };
    }

    /**
     * Mapea del enum del dominio al enum JPA.
     */
    private static MaintenanceStatusJpaEnum mapDomainStatusToJpaStatus(MaintenanceStatus domainStatus) {
        if (domainStatus == null) {
            return MaintenanceStatusJpaEnum.OPERATIONAL;
        }

        return switch (domainStatus) {
            case OPERATIONAL -> MaintenanceStatusJpaEnum.OPERATIONAL;
            case UNDER_MAINTENANCE -> MaintenanceStatusJpaEnum.UNDER_MAINTENANCE;
            case OUT_OF_SERVICE -> MaintenanceStatusJpaEnum.OUT_OF_SERVICE;
            case NEEDS_MAINTENANCE -> MaintenanceStatusJpaEnum.NEEDS_MAINTENANCE;
        };
    }
}
