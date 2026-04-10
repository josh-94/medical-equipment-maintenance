package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;
import com.tecsup.medicalequipment.domain.repository.MaintenanceScheduleRepository;
import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.mapper.MaintenanceScheduleMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa MaintenanceScheduleRepository del dominio usando Spring Data JPA.
 */
@Component
public class MaintenanceScheduleRepositoryAdapter implements MaintenanceScheduleRepository {

    private final MaintenanceScheduleJpaRepositoryInterface jpaRepository;

    public MaintenanceScheduleRepositoryAdapter(MaintenanceScheduleJpaRepositoryInterface jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(MaintenanceSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("El cronograma no puede ser nulo");
        }

        var jpaEntity = MaintenanceScheduleMapper.toJpaEntity(schedule);
        jpaRepository.save(jpaEntity);
    }

    @Override
    public Optional<MaintenanceSchedule> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        Optional<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntity =
            jpaRepository.findById(id);

        return jpaEntity.map(MaintenanceScheduleMapper::toDomain);
    }

    @Override
    public List<MaintenanceSchedule> findByEquipmentId(String equipmentId) {
        if (equipmentId == null || equipmentId.isBlank()) {
            throw new IllegalArgumentException("El ID del equipo no puede estar vacío");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntities =
            jpaRepository.findByEquipmentId(equipmentId);

        return jpaEntities.stream()
            .map(MaintenanceScheduleMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceSchedule> findByTechnicianId(String technicianId) {
        if (technicianId == null || technicianId.isBlank()) {
            throw new IllegalArgumentException("El ID del técnico no puede estar vacío");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntities =
            jpaRepository.findByTechnicianId(technicianId);

        return jpaEntities.stream()
            .map(MaintenanceScheduleMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceSchedule> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntities =
            jpaRepository.findByScheduledDateBetween(startDate, endDate);

        return jpaEntities.stream()
            .map(MaintenanceScheduleMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceSchedule> findPendingSchedules() {
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntities =
            jpaRepository.findByCompletedFalse();

        return jpaEntities.stream()
            .map(MaintenanceScheduleMapper::toDomain)
            .toList();
    }

    @Override
    public List<MaintenanceSchedule> findAll() {
        List<com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity> jpaEntities =
            jpaRepository.findAll();

        return jpaEntities.stream()
            .map(MaintenanceScheduleMapper::toDomain)
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
    public boolean existsForTechnicianAtDateTime(String technicianId, LocalDateTime dateTime) {
        if (technicianId == null || technicianId.isBlank() || dateTime == null) {
            throw new IllegalArgumentException("El ID del técnico y la fecha no pueden ser nulos");
        }

        // Verificar conflictos en un rango de ±1 hora
        LocalDateTime startDateTime = dateTime.minusHours(1);
        LocalDateTime endDateTime = dateTime.plusHours(1);

        return jpaRepository.existsForTechnicianAtDateTime(technicianId, startDateTime, endDateTime);
    }
}
