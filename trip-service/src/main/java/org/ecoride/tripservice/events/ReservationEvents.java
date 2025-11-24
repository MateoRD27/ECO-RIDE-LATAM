package org.ecoride.tripservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class ReservationEvents {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationRequested {
        private UUID reservationId;
        private UUID tripId;
        private UUID passengerId;
        private BigDecimal amount;
        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationConfirmed {
        private UUID reservationId;
        private UUID tripId;
        private UUID passengerId;
        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationCancelled {
        private UUID reservationId;
        private UUID tripId;
        private UUID passengerId;
        private String reason;
        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentAuthorized {
        private UUID reservationId;
        private UUID paymentIntentId;
        private UUID chargeId;
        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentFailed {
        private UUID reservationId;
        private UUID passengerId;
        private String reason;
        private String correlationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TripCompleted {
        private UUID tripId;
        private UUID driverId;
        private String correlationId;
    }
}