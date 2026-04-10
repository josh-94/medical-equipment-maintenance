package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.domain.model.MaintenanceVisit;
import com.tecsup.medicalequipment.domain.repository.MaintenanceVisitRepository;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper.MaintenanceVisitMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa MaintenanceVisitRepository del dominio usando Spring Data JPA.
 */
@Component
public class MaintenanceVisitRepositoryAdapter implements MaintenanceVisitRepository {

    private final MaintenanceVisitJpaRepositoryInterface jpaRepository;

    public MaintenanceVisitRepositoryAdapter(MaintenanceVisitJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(MaintenanceVisit visit) {
        if (visit == null) {
            throw new IllegalArgumentException("La visita no puede ser nula");
        }

        var jpaEntity = MaintenanceVisitMapper.toJpaEntity(visit);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public Optional<MaintenanceVisit> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity> jpaEntity =
            jpaRepository.findById(id);

        return jpaEntity.map(MaintenanceVisitMapper::toDomain);
    }

    @Override
    public List<MaintenanceVisit> findByMaintenanceScheduleId(String scheduleId) {
        if (scheduleId == null || scheduleId.isBlank()) {
            throw new IllegalArgumentException("El ID del cronograma no puede estar vacío");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity> jpaEntities =
            jpaRepository.findByMaintenanceScheduleId(scheduleId);

        return jpaEntities.stream()
            .map(MaintenanceVisitMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceVisit> findByTechnicianId(String technicianId) {
        if (technicianId == null || technicianId.isBlank()) {
            throw new IllegalArgumentException("El ID del técnico no puede estar vacío");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity> jpaEntities =
            jpaRepository.findByTechnicianId(technicianId);

        return jpaEntities.stream()
            .map(MaintenanceVisitMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceVisit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity> jpaEntities =
            jpaRepository.findByVisitDateBetween(startDate, endDate);

        return jpaEntities.stream()
            .map(MaintenanceVisitMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceVisit> findAll() {
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity> jpaEntities =
            jpaRepository.findAll();

        return jpaEntities.stream()
            .map(MaintenanceVisitMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        jpaRepository.deleteById(id);
    }

    @Override
    public long countVisitsForTechnicianAtDateTime(String technicianId, LocalDateTime dateTime) {
        if (technicianId == null || technicianId.isBlank() || dateTime == null) {
            throw new IllegalArgumentException("El ID del técnico y la fecha no pueden ser nulos");
        }

        // Contar visitas en un rango de ±1 hora
        LocalDateTime startDateTime = dateTime.minusHours(1);
        LocalDateTime endDateTime = dateTime.plusHours(1);

        return jpaRepository.countVisitsForTechnicianAtDateTime(technicianId, startDateTime, endDateTime);
    }
}
