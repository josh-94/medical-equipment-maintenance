package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.EquipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz de Spring Data JPA para EquipmentJpaEntity.
 * Proporciona métodos CRUD básicos y consultas personalizadas.
 * NO es el repositorio del dominio, es la interfaz técnica de persistencia.
 */
@Repository
public interface EquipmentJpaRepositoryInterface extends JpaRepository<EquipmentJpaEntity, String> {

    /**
     * Busca un equipo por número de serie.
     */
    Optional<EquipmentJpaEntity> findBySerialNumber(String serialNumber);

    /**
     * Verifica si existe un equipo con un número de serie dado.
     */
    boolean existsBySerialNumber(String serialNumber);
}
