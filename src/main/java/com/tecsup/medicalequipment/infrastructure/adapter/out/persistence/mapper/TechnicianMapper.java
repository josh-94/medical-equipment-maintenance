package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper;

import com.tecsup.medicalequipment.domain.model.Technician;
import com.tecsup.medicalequipment.domain.model.valueobjects.Email;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity;

/**
 * Mapper para convertir entre Technician (dominio) y TechnicianJpaEntity (persistencia).
 */
public class TechnicianMapper {

    public static Technician toDomain(TechnicianJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return Technician.reconstruct(
            jpaEntity.getId(),
            jpaEntity.getName(),
            Email.create(jpaEntity.getEmail()),
            jpaEntity.getSpecialization()
        );
    }

    public static TechnicianJpaEntity toJpaEntity(Technician technician) {
        if (technician == null) {
            return null;
        }

        return new TechnicianJpaEntity(
            technician.getId(),
            technician.getName(),
            technician.getEmail().getValue(),
            technician.getSpecialization()
        );
    }
}
