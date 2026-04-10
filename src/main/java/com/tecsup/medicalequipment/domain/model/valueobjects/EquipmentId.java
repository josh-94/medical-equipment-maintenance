package com.tecsup.medicalequipment.domain.model.valueobjects;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un equipo.
 */
public class EquipmentId {

    private final String value;

    private EquipmentId(String value) {
        this.value = value;
    }

    public static EquipmentId create(String id) {
        if (id == null || id.isBlank()) {
            throw new DomainException("El ID del equipo no puede estar vacío");
        }
        return new EquipmentId(id.trim());
    }

    public static EquipmentId generate() {
        return new EquipmentId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentId that = (EquipmentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
