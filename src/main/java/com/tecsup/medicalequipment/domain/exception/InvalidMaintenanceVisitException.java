package com.tecsup.medicalequipment.domain.exception;

/**
 * Se lanza cuando se intenta registrar una visita de mantenimiento de forma inválida.
 */
public class InvalidMaintenanceVisitException extends DomainException {

    public InvalidMaintenanceVisitException(String message) {
        super(message);
    }
}
