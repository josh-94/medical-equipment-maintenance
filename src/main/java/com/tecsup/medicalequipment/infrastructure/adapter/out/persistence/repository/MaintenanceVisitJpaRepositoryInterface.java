package com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.repository;

import com.tecsup.medicalequipment.infrastructure.adapter.out.persistence.entity.MaintenanceVisitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz de Spring Data JPA para MaintenanceVisitJpaEntity.
 */
@Repository
public interface MaintenanceVisitJpaRepositoryInterface extends JpaRepository<MaintenanceVisitJpaEntity, String> {

    /**
     * Busca visitas por cronograma de mantenimiento.
     */
    List<MaintenanceVisitJpaEntity> findByMaintenanceScheduleId(String scheduleId);

    /**
     * Busca visitas por técnico.
     */
    List<MaintenanceVisitJpaEntity> findByTechnicianId(String technicianId);

    /**
     * Busca visitas en un rango de fechas.
     */
    List<MaintenanceVisitJpaEntity> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Contar visitas para un técnico en una fecha determinada (±1 hora).
     * REGLA: Evita dos visitas del mismo técnico en el mismo horario.
     */
    @Query("SELECT COUNT(v) FROM MaintenanceVisitJpaEntity v " +
           "WHERE v.technicianId = :technicianId " +
           "AND v.visitDate >= :startDateTime " +
           "AND v.visitDate < :endDateTime")
    long countVisitsForTechnicianAtDateTime(@Param("technicianId") String technicianId,
                                           @Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime);
}
