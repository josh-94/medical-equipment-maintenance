package com.tecsup.medicalequipment.domain.model.valueobjects;

import com.tecsup.medicalequipment.domain.exception.DomainException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa un correo electrónico válido.
 */
public class Email {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email create(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainException("El correo electrónico no puede estar vacío");
        }

        String trimmedEmail = email.trim();
        
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new DomainException("El formato del correo electrónico es inválido: " + email);
        }

        return new Email(trimmedEmail.toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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
