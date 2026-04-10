package com.tecsup.medicalequipment.domain.model.valueobjects;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import java.util.Objects;

/**
 * Value Object que representa el número de serie de un equipo.
 * Garantiza que sea único y no nulo.
 */
public class SerialNumber {

    private final String value;

    private SerialNumber(String value) {
        this.value = value;
    }

    public static SerialNumber create(String serialNumber) {
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new DomainException("El número de serie no puede estar vacío");
        }

        String trimmedSerial = serialNumber.trim().toUpperCase();
        
        if (trimmedSerial.length() < 3) {
            throw new DomainException("El número de serie debe tener al menos 3 caracteres");
        }

        return new SerialNumber(trimmedSerial);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialNumber that = (SerialNumber) o;
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
