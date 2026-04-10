package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.domain.model.Institution;
import com.tecsup.medicalequipment.domain.repository.InstitutionRepository;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper.InstitutionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa InstitutionRepository del dominio usando Spring Data JPA.
 */
@Component
public class InstitutionRepositoryAdapter implements InstitutionRepository {

    private final InstitutionJpaRepositoryInterface jpaRepository;

    public InstitutionRepositoryAdapter(InstitutionJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Institution institution) {
        if (institution == null) {
            throw new IllegalArgumentException("La institución no puede ser nula");
        }

        var jpaEntity = InstitutionMapper.toJpaEntity(institution);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public Optional<Institution> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.InstitutionJpaEntity> jpaEntity =
            jpaRepository.findById(id);

        return jpaEntity.map(InstitutionMapper::toDomain);
    }

    @Override
    public List<Institution> findByNameContaining(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.InstitutionJpaEntity> jpaEntities =
            jpaRepository.findByNameContainingIgnoreCase(name);

        return jpaEntities.stream()
            .map(InstitutionMapper::toDomain)
            .toList();
    }

    @Override
    public List<Institution> findAll() {
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.InstitutionJpaEntity> jpaEntities =
            jpaRepository.findAll();

        return jpaEntities.stream()
            .map(InstitutionMapper::toDomain)
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
