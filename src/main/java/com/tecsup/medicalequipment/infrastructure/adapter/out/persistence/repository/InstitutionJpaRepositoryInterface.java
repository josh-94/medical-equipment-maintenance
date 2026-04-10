package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.InstitutionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz de Spring Data JPA para InstitutionJpaEntity.
 */
@Repository
public interface InstitutionJpaRepositoryInterface extends JpaRepository<InstitutionJpaEntity, String> {

    /**
     * Busca instituciones por nombre (búsqueda parcial).
     */
    List<InstitutionJpaEntity> findByNameContainingIgnoreCase(String name);
}
