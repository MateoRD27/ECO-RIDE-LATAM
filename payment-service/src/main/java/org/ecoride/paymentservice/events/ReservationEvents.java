package org.ecoride.paymentservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEvents {
    //  Lo que se recibe de Trip-Service
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

    // Lo que se responde si el cobro es EXITOSO
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

    //  Lo que se responde si el cobro FALLA
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentFailed {
        private UUID reservationId;
        private String reason;
        private String correlationId;
    }

}
