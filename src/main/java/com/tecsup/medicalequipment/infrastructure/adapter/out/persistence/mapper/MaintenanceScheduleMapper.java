package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper;

import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.model.MaintenanceType;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceTypeJpaEnum;

/**
 * Mapper para convertir entre MaintenanceSchedule (dominio) y MaintenanceScheduleJpaEntity (persistencia).
 */
public class MaintenanceScheduleMapper {

    public static MaintenanceSchedule toDomain(MaintenanceScheduleJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return MaintenanceSchedule.reconstruct(
            jpaEntity.getId(),
            jpaEntity.getEquipmentId(),
            jpaEntity.getScheduledDate(),
            mapJpaTypeToMaintenanceType(jpaEntity.getType()),
            jpaEntity.getTechnicianId(),
            jpaEntity.getActualStartDate(),
            jpaEntity.getActualEndDate(),
            jpaEntity.isCompleted()
        );
    }

    public static MaintenanceScheduleJpaEntity toJpaEntity(MaintenanceSchedule schedule) {
        if (schedule == null) {
            return null;
        }

        return new MaintenanceScheduleJpaEntity(
            schedule.getId(),
            schedule.getEquipmentId(),
            schedule.getScheduledDate(),
            mapMaintenanceTypeToJpaType(schedule.getType()),
            schedule.getTechnicianId(),
            schedule.getActualStartDate(),
            schedule.getActualEndDate(),
            schedule.isCompleted()
        );
    }

    private static MaintenanceType mapJpaTypeToMaintenanceType(MaintenanceTypeJpaEnum jpaType) {
        if (jpaType == null) {
            return MaintenanceType.PREVENTIVE;
        }

        return switch (jpaType) {
            case PREVENTIVE -> MaintenanceType.PREVENTIVE;
            case CORRECTIVE -> MaintenanceType.CORRECTIVE;
            case INSPECTION -> MaintenanceType.INSPECTION;
            case INSTALLATION -> MaintenanceType.INSTALLATION;
            case DECOMMISSIONING -> MaintenanceType.DECOMMISSIONING;
        };
    }

    private static MaintenanceTypeJpaEnum mapMaintenanceTypeToJpaType(MaintenanceType domainType) {
        if (domainType == null) {
            return MaintenanceTypeJpaEnum.PREVENTIVE;
        }

        return switch (domainType) {
            case PREVENTIVE -> MaintenanceTypeJpaEnum.PREVENTIVE;
            case CORRECTIVE -> MaintenanceTypeJpaEnum.CORRECTIVE;
            case INSPECTION -> MaintenanceTypeJpaEnum.INSPECTION;
            case INSTALLATION -> MaintenanceTypeJpaEnum.INSTALLATION;
            case DECOMMISSIONING -> MaintenanceTypeJpaEnum.DECOMMISSIONING;
        };
    }
}
