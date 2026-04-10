package com.tecsup.medicalequipment.domain.exception;

/**
 * Excepción base del dominio.
 * Se lanza cuando se viola una regla de negocio.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
