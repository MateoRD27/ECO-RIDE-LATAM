package org.ecoride.notificationservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

public class ReservationEvents {
    // Evento emitido por TripService cuando se confirma todo
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

    // Evento emitido por PaymentService cuando falla el cobro
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentFailed {
        private UUID reservationId;
        private String reason;
        private String correlationId;
    }

    // Evento emitido por TripService cuando se cancela (por pago fallido o manual)
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
}
