package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.domain.model.Equipment;
import com.tecsup.medicalequipment.domain.repository.EquipmentRepository;
import com.tecsup.medicalequipment.domain.model.valueobjects.EquipmentId;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper.EquipmentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa EquipmentRepository del dominio usando Spring Data JPA.
 * 
 * Esta es la IMPLEMENTACION del contrato del dominio.
 * La capa de dominio depende de la interfaz EquipmentRepository.
 * Esta clase adapta la infraestructura JPA al contrato del dominio.
 * 
 * Patrón Adapter: adapta EquipmentJpaRepositoryInterface (JPA) a EquipmentRepository (dominio).
 */
@Component
public class EquipmentRepositoryAdapter implements EquipmentRepository {

    private final EquipmentJpaRepositoryInterface jpaRepository;

    public EquipmentRepositoryAdapter(EquipmentJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Guardar un nuevo equipo o actualizar uno existente.
     */
    @Override
    public void save(Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo");
        }

        // Mapear Equipment (dominio) a EquipmentJpaEntity (persistencia)
        var jpaEntity = EquipmentMapper.toJpaEntity(equipment);

        // Guardar usando JPA
        jpaRepository.save(jpaEntity);
    }

    /**
     * Buscar un equipo por su ID.
     */
    @Override
    public Optional<Equipment> findById(EquipmentId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del equipo no puede ser nulo");
        }

        // Buscar usando JPA
        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.EquipmentJpaEntity> jpaEntity =
            jpaRepository.findById(id.getValue());

        // Mapear la entidad JPA a dominio si existe
        return jpaEntity.map(EquipmentMapper::toDomain);
    }

    /**
     * Buscar un equipo por su número de serie.
     */
    @Override
    public Optional<Equipment> findBySerialNumber(String serialNumber) {
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("El número de serie no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.EquipmentJpaEntity> jpaEntity =
            jpaRepository.findBySerialNumber(serialNumber.toUpperCase());

        return jpaEntity.map(EquipmentMapper::toDomain);
    }

    /**
     * Obtener todos los equipos.
     */
    @Override
    public List<Equipment> findAll() {
        // Buscar todos usando JPA
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.EquipmentJpaEntity> jpaEntities =
            jpaRepository.findAll();

        // Mapear todas las entidades JPA a dominio
        return jpaEntities.stream()
            .map(EquipmentMapper::toDomain)
            .toList();
    }

    /**
     * Eliminar un equipo por su ID.
     */
    @Override
    public void deleteById(EquipmentId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del equipo no puede ser nulo");
        }

        jpaRepository.deleteById(id.getValue());
    }

    /**
     * Verificar si existe un equipo con un número de serie dado.
     */
    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("El número de serie no puede estar vacío");
        }

        return jpaRepository.existsBySerialNumber(serialNumber.toUpperCase());
    }
}
