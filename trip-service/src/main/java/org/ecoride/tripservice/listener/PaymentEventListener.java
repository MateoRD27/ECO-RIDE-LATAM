package org.ecoride.tripservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.tripservice.events.ReservationEvents;
import org.ecoride.tripservice.service.TripService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final TripService tripService;

    @KafkaListener(
            topics = "payment-authorized",
            groupId = "trip-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentAuthorized(
            @Payload ReservationEvents.PaymentAuthorized event,
            Acknowledgment acknowledgment) {

        log.info("[{}] Recibido evento PaymentAuthorized para reserva: {}",
                event.getCorrelationId(), event.getReservationId());

        try {
            tripService.confirmReservation(
                    event.getReservationId(),
                    event.getCorrelationId()
            );
            acknowledgment.acknowledge();
            log.info("[{}] Reserva confirmada exitosamente", event.getCorrelationId());
        } catch (Exception e) {
            log.error("[{}] Error confirmando reserva: {}",
                    event.getCorrelationId(), e.getMessage(), e);
            // No hacer acknowledge para que se reintente
        }
    }

    @KafkaListener(
            topics = "payment-failed",
            groupId = "trip-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentFailed(
            @Payload ReservationEvents.PaymentFailed event,
            Acknowledgment acknowledgment) {

        log.warn("[{}] Recibido evento PaymentFailed para reserva: {}, reason: {}",
                event.getCorrelationId(), event.getReservationId(), event.getReason());

        try {
            tripService.cancelReservation(
                    event.getReservationId(),
                    "PAYMENT_FAILED: " + event.getReason(),
                    event.getCorrelationId()
            );
            acknowledgment.acknowledge();
            log.info("[{}] Compensación completada: reserva cancelada y asiento liberado",
                    event.getCorrelationId());
        } catch (Exception e) {
            log.error("[{}] Error en compensación: {}",
                    event.getCorrelationId(), e.getMessage(), e);
        }
    }
}