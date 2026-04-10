package com.tecsup.medicalequipment.domain.repository;

import com.tecsup.medicalequipment.domain.model.MaintenanceVisit;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad MaintenanceVisit.
 * Define contratos sin dependencia de la infraestructura.
 */
public interface MaintenanceVisitRepository {

    /**
     * Guardar una nueva visita o actualizar una existente.
     */
    void save(MaintenanceVisit visit);

    /**
     * Buscar una visita por su ID.
     */
    Optional<MaintenanceVisit> findById(String id);

    /**
     * Buscar visitas por cronograma de mantenimiento.
     */
    List<MaintenanceVisit> findByMaintenanceScheduleId(String scheduleId);

    /**
     * Buscar visitas por técnico.
     */
    List<MaintenanceVisit> findByTechnicianId(String technicianId);

    /**
     * Buscar visitas en un rango de fechas.
     */
    List<MaintenanceVisit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtener todas las visitas.
     */
    List<MaintenanceVisit> findAll();

    /**
     * Eliminar una visita por su ID.
     */
    void deleteById(String id);

    /**
     * Contar visitas para un técnico en una fecha determinada.
     * REGLA: Evita dos visitas del mismo técnico en el mismo horario.
     */
    long countVisitsForTechnicianAtDateTime(String technicianId, LocalDateTime dateTime);
}
