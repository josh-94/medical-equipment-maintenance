package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.domain.model.Technician;
import com.tecsup.medicalequipment.domain.repository.TechnicianRepository;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper.TechnicianMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa TechnicianRepository del dominio usando Spring Data JPA.
 */
@Component
public class TechnicianRepositoryAdapter implements TechnicianRepository {

    private final TechnicianJpaRepositoryInterface jpaRepository;

    public TechnicianRepositoryAdapter(TechnicianJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Technician technician) {
        if (technician == null) {
            throw new IllegalArgumentException("El técnico no puede ser nulo");
        }

        var jpaEntity = TechnicianMapper.toJpaEntity(technician);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public Optional<Technician> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity> jpaEntity =
            jpaRepository.findById(id);

        return jpaEntity.map(TechnicianMapper::toDomain);
    }

    @Override
    public Optional<Technician> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity> jpaEntity =
            jpaRepository.findByEmail(email.toLowerCase());

        return jpaEntity.map(TechnicianMapper::toDomain);
    }

    @Override
    public List<Technician> findBySpecialization(String specialization) {
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("La especialización no puede estar vacía");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity> jpaEntities =
            jpaRepository.findBySpecializationIgnoreCase(specialization);

        return jpaEntities.stream()
            .map(TechnicianMapper::toDomain)
            .toList();
    }

    @Override
    public List<Technician> findAll() {
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.TechnicianJpaEntity> jpaEntities =
            jpaRepository.findAll();

        return jpaEntities.stream()
            .map(TechnicianMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        jpaRepository.deleteById(id);
    }
}
