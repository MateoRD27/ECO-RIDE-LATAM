package org.ecoride.passengerservice.exception;

public class DriverProfileAlreadyExistsException extends RuntimeException {
    public DriverProfileAlreadyExistsException(String message) {
        super(message);
    }
}
