package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.model.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitarios para la entidad Technician del dominio.
 * Valida que se respeten las invariantes de la entidad.
 */
@DisplayName("Technician Domain Model Tests")
class TechnicianTest {

    @Test
    @DisplayName("Debe crear un técnico válido")
    void testCreateValidTechnician() {
        // Arrange
        String name = "Carlos García";
        Email email = Email.create("carlos@example.com");
        String specialization = "Cardiología";

        // Act
        Technician technician = Technician.create(name, email, specialization);

        // Assert
        assertNotNull(technician);
        assertEquals(name, technician.getName());
        assertEquals(email, technician.getEmail());
        assertEquals(specialization, technician.getSpecialization());
        assertNotNull(technician.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre está vacío")
    void testCreateTechnicianWithEmptyName() {
        // Arrange
        Email email = Email.create("carlos@example.com");
        String specialization = "Cardiología";

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            Technician.create("", email, specialization);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email es nulo")
    void testCreateTechnicianWithNullEmail() {
        // Arrange
        String name = "Carlos García";
        String specialization = "Cardiología";

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            Technician.create(name, null, specialization);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si la especialización está vacía")
    void testCreateTechnicianWithEmptySpecialization() {
        // Arrange
        String name = "Carlos García";
        Email email = Email.create("carlos@example.com");

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            Technician.create(name, email, "");
        });
    }

    @Test
    @DisplayName("Técnico con el mismo ID deben ser iguales")
    void testTechnicianEquality() {
        // Arrange
        Email email = Email.create("carlos@example.com");
        Technician tech1 = Technician.reconstruct("tech-123", "Carlos", email, "Cardiología");
        Technician tech2 = Technician.reconstruct("tech-123", "Carlos", email, "Cardiología");

        // Act & Assert
        assertEquals(tech1, tech2);
        assertEquals(tech1.hashCode(), tech2.hashCode());
    }
}
