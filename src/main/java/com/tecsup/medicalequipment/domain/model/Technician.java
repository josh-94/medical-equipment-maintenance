package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import com.tecsup.medicalequipment.domain.model.valueobjects.Email;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa un Técnico de Mantenimiento.
 */
public class Technician {

    private final String id;
    private final String name;
    private final Email email;
    private final String specialization;

    private Technician(String id, String name, Email email, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
    }

    /**
     * Factory method para crear un nuevo técnico.
     */
    public static Technician create(String name, Email email, String specialization) {
        validateTechnician(name, email, specialization);
        return new Technician(UUID.randomUUID().toString(), name, email, specialization);
    }

    /**
     * Reconstruir una entidad existente desde persistencia.
     */
    public static Technician reconstruct(String id, String name, Email email, String specialization) {
        validateTechnician(name, email, specialization);
        return new Technician(id, name, email, specialization);
    }

    private static void validateTechnician(String name, Email email, String specialization) {
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre del técnico no puede estar vacío");
        }
        if (email == null) {
            throw new DomainException("El correo del técnico no puede ser nulo");
        }
        if (specialization == null || specialization.isBlank()) {
            throw new DomainException("La especialización del técnico no puede estar vacía");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technician that = (Technician) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Technician{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email=" + email +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
