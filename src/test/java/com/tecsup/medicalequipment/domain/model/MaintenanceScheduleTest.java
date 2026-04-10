package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.InvalidMaintenanceScheduleDateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitarios para la entidad MaintenanceSchedule del dominio.
 * Valida que se respeten las reglas de negocio.
 */
@DisplayName("MaintenanceSchedule Domain Model Tests")
class MaintenanceScheduleTest {

    @Test
    @DisplayName("Debe crear un cronograma válido para fecha futura")
    void testCreateValidSchedule() {
        // Arrange
        String equipmentId = "eq-123";
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);
        MaintenanceType type = MaintenanceType.PREVENTIVE;
        String technicianId = "tech-456";

        // Act
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            equipmentId, futureDate, type, technicianId
        );

        // Assert
        assertNotNull(schedule);
        assertEquals(equipmentId, schedule.getEquipmentId());
        assertEquals(futureDate, schedule.getScheduledDate());
        assertEquals(type, schedule.getType());
        assertEquals(technicianId, schedule.getTechnicianId());
        assertFalse(schedule.isCompleted());
    }

    @Test
    @DisplayName("REGLA: No debe permitir programar mantenimiento en fecha pasada")
    void testCannotCreateScheduleInPastDate() {
        // Arrange
        String equipmentId = "eq-123";
        LocalDateTime pastDate = LocalDateTime.now().minusHours(2);
        MaintenanceType type = MaintenanceType.PREVENTIVE;
        String technicianId = "tech-456";

        // Act & Assert
        assertThrows(InvalidMaintenanceScheduleDateException.class, () -> {
            MaintenanceSchedule.create(equipmentId, pastDate, type, technicianId);
        });
    }

    @Test
    @DisplayName("Debe poder iniciar un mantenimiento")
    void testStartMaintenance() {
        // Arrange
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            "eq-123",
            LocalDateTime.now().plusDays(1),
            MaintenanceType.PREVENTIVE,
            "tech-456"
        );

        // Act
        schedule.startMaintenance();

        // Assert
        assertNotNull(schedule.getActualStartDate());
        assertFalse(schedule.isCompleted());
    }

    @Test
    @DisplayName("Debe poder completar un mantenimiento que ya inició")
    void testCompleteMaintenance() {
        // Arrange
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            "eq-123",
            LocalDateTime.now().plusDays(1),
            MaintenanceType.PREVENTIVE,
            "tech-456"
        );
        schedule.startMaintenance();

        // Act
        schedule.completeMaintenance();

        // Assert
        assertNotNull(schedule.getActualEndDate());
        assertTrue(schedule.isCompleted());
    }

    @Test
    @DisplayName("No debe poder completar sin haber iniciado")
    void testCannotCompleteWithoutStarting() {
        // Arrange
        MaintenanceSchedule schedule = MaintenanceSchedule.create(
            "eq-123",
            LocalDateTime.now().plusDays(1),
            MaintenanceType.PREVENTIVE,
            "tech-456"
        );

        // Act & Assert
        assertThrows(Exception.class, schedule::completeMaintenance);
    }

    @Test
    @DisplayName("Cronogramas con el mismo ID deben ser iguales")
    void testScheduleEquality() {
        // Arrange
        MaintenanceSchedule schedule1 = MaintenanceSchedule.reconstruct(
            "sch-123", "eq-123", LocalDateTime.now().plusDays(1),
            MaintenanceType.PREVENTIVE, "tech-456",
            null, null, false
        );
        MaintenanceSchedule schedule2 = MaintenanceSchedule.reconstruct(
            "sch-123", "eq-123", LocalDateTime.now().plusDays(1),
            MaintenanceType.PREVENTIVE, "tech-456",
            null, null, false
        );

        // Act & Assert
        assertEquals(schedule1, schedule2);
        assertEquals(schedule1.hashCode(), schedule2.hashCode());
    }
}
