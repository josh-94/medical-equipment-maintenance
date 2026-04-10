package com.tecsup.medicalequipment.domain.exception;

/**
 * Se lanza cuando hay conflicto de horarios para un mismo técnico.
 */
public class ConflictingScheduleException extends DomainException {

    public ConflictingScheduleException(String message) {
        super(message);
    }
}
