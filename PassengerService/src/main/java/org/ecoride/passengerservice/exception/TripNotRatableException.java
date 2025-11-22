package org.ecoride.passengerservice.exception;

public class TripNotRatableException extends RuntimeException {
    public TripNotRatableException(String message) {
        super(message);
    }
}