package com.tecsup.medicalequipment.domain.model;

import com.tecsup.medicalequipment.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa una Institución donde se ubican y mantienen los equipos médicos.
 */
public class Institution {

    private final String id;
    private final String name;
    private final String service;

    private Institution(String id, String name, String service) {
        this.id = id;
        this.name = name;
        this.service = service;
    }

    /**
     * Factory method para crear una nueva institución.
     */
    public static Institution create(String name, String service) {
        validateInstitution(name, service);
        return new Institution(UUID.randomUUID().toString(), name, service);
    }

    /**
     * Reconstruir una entidad existente desde persistencia.
     */
    public static Institution reconstruct(String id, String name, String service) {
        validateInstitution(name, service);
        return new Institution(id, name, service);
    }

    private static void validateInstitution(String name, String service) {
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre de la institución no puede estar vacío");
        }
        if (service == null || service.isBlank()) {
            throw new DomainException("El servicio de la institución no puede estar vacío");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Institution that = (Institution) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
