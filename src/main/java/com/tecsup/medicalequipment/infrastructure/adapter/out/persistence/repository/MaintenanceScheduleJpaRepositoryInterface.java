package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceScheduleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz de Spring Data JPA para MaintenanceScheduleJpaEntity.
 */
@Repository
public interface MaintenanceScheduleJpaRepositoryInterface extends JpaRepository<MaintenanceScheduleJpaEntity, String> {

    /**
     * Busca cronogramas por equipo.
     */
    List<MaintenanceScheduleJpaEntity> findByEquipmentId(String equipmentId);

    /**
     * Busca cronogramas por técnico.
     */
    List<MaintenanceScheduleJpaEntity> findByTechnicianId(String technicianId);

    /**
     * Busca cronogramas en un rango de fechas.
     */
    List<MaintenanceScheduleJpaEntity> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca cronogramas no completados.
     */
    List<MaintenanceScheduleJpaEntity> findByCompletedFalse();

    /**
     * Verifica si existe un cronograma para un técnico en una fecha determinada (±1 hora).
     * REGLA: Evita conflictos de horarios.
     */
    @Query("SELECT COUNT(m) > 0 FROM MaintenanceScheduleJpaEntity m " +
           "WHERE m.technicianId = :technicianId " +
           "AND m.scheduledDate >= :startDateTime " +
           "AND m.scheduledDate < :endDateTime")
    boolean existsForTechnicianAtDateTime(@Param("technicianId") String technicianId,
                                         @Param("startDateTime") LocalDateTime startDateTime,
                                         @Param("endDateTime") LocalDateTime endDateTime);
}
