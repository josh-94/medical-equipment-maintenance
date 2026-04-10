package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de Spring Data JPA para TechnicianJpaEntity.
 */
@Repository
public interface TechnicianJpaRepositoryInterface extends JpaRepository<TechnicianJpaEntity, String> {

    /**
     * Busca un técnico por correo electrónico.
     */
    Optional<TechnicianJpaEntity> findByEmail(String email);

    /**
     * Busca técnicos por especialización.
     */
    List<TechnicianJpaEntity> findBySpecializationIgnoreCase(String specialization);
}
