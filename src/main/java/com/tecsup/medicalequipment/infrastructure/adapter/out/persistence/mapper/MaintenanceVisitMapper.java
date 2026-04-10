package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper;

import com.tecsup.medicalequipment.domain.model.MaintenanceVisit;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity;

/**
 * Mapper para convertir entre MaintenanceVisit (dominio) y MaintenanceVisitJpaEntity (persistencia).
 */
public class MaintenanceVisitMapper {

    public static MaintenanceVisit toDomain(MaintenanceVisitJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return MaintenanceVisit.reconstruct(
            jpaEntity.getId(),
            jpaEntity.getMaintenanceScheduleId(),
            jpaEntity.getVisitDate(),
            jpaEntity.getObservations(),
            jpaEntity.getTechnicianId()
        );
    }

    public static MaintenanceVisitJpaEntity toJpaEntity(MaintenanceVisit visit) {
        if (visit == null) {
            return null;
        }

        return new MaintenanceVisitJpaEntity(
            visit.getId(),
            visit.getMaintenanceScheduleId(),
            visit.getVisitDate(),
            visit.getObservations(),
            visit.getTechnicianId()
        );
    }
}
