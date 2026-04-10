package com.tecsup.medicalequipment.domain.exception;

/**
 * Se lanza cuando se intenta programar un mantenimiento en una fecha pasada.
 */
public class InvalidMaintenanceScheduleDateException extends DomainException {

    public InvalidMaintenanceScheduleDateException(String message) {
        super(message);
    }
}
