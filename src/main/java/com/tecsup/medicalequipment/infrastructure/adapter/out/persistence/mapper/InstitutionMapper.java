package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper;

import com.tecsup.medicalequipment.domain.model.Institution;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.InstitutionJpaEntity;

/**
 * Mapper para convertir entre Institution (dominio) e InstitutionJpaEntity (persistencia).
 */
public class InstitutionMapper {

    public static Institution toDomain(InstitutionJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return Institution.reconstruct(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getService()
        );
    }

    public static InstitutionJpaEntity toJpaEntity(Institution institution) {
        if (institution == null) {
            return null;
        }

        return new InstitutionJpaEntity(
            institution.getId(),
            institution.getName(),
            institution.getService()
        );
    }
}
