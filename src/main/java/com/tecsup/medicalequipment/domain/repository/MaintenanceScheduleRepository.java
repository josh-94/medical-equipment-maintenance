package com.tecsup.medicalequipment.domain.repository;

import com.tecsup.medicalequipment.domain.model.MaintenanceSchedule;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * Interfaz de repositorio para la entidad MaintenanceSchedule.
 * Define contratos sin dependencia de la infraestructura.
 */
public interface MaintenanceScheduleRepository {

    /**
     * Guardar un nuevo cronograma o actualizar uno existente.
     */
    void save(MaintenanceSchedule schedule);

    /**
     * Buscar un cronograma por su ID.
     */
    Optional<MaintenanceSchedule> findById(String id);

    /**
     * Buscar cronogramas por equipo.
     */
    List<MaintenanceSchedule> findByEquipmentId(String equipmentId);

    /**
     * Buscar cronogramas por técnico.
     */
    List<MaintenanceSchedule> findByTechnicianId(String technicianId);

    /**
     * Buscar cronogramas en un rango de fechas.
     */
    List<MaintenanceSchedule> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Buscar cronogramas no completados.
     */
    List<MaintenanceSchedule> findPendingSchedules();

    /**
     * Obtener todos los cronogramas.
     */
    List<MaintenanceSchedule> findAll();

    /**
     * Eliminar un cronograma por su ID.
     */
    void deleteById(String id);

    /**
     * Verificar si hay cronogramas para un técnico en una fecha determinada.
     * REGLA: Evita conflictos de horarios.
     */
    boolean existsForTechnicianAtDateTime(String technicianId, LocalDateTime dateTime);
}
