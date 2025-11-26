package org.ecoride.tripservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.tripservice.events.ReservationEvents;
import org.ecoride.tripservice.service.TripService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final TripService tripService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payment-authorized",
            groupId = "trip-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentAuthorized(
            @Payload String rawEvent,
            Acknowledgment acknowledgment) {

        try {
            // Deserializar el string al objeto real
            ReservationEvents.PaymentAuthorized event =
                    objectMapper.readValue(rawEvent, ReservationEvents.PaymentAuthorized.class);

            log.info("[{}] Recibido PaymentAuthorized", event.getCorrelationId());

            tripService.confirmReservation(
                    event.getReservationId(),
                    event.getCorrelationId()
            );

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Error procesando evento PaymentAuthorized: {}", e.getMessage(), e);
        }
    }


    @KafkaListener(
            topics = "payment-failed",
            groupId = "trip-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentFailed(String rawEvent, Acknowledgment acknowledgment) {

        try {
            // Convertir JSON → Evento
            ReservationEvents.PaymentFailed event =
                    objectMapper.readValue(rawEvent, ReservationEvents.PaymentFailed.class);

            log.warn("[{}] Recibido evento PaymentFailed para reserva: {}, reason: {}",
                    event.getCorrelationId(), event.getReservationId(), event.getReason());

            // Ejecutar compensación
            tripService.cancelReservation(
                    event.getReservationId(),
                    "PAYMENT_FAILED: " + event.getReason(),
                    event.getCorrelationId()
            );

            acknowledgment.acknowledge();
            log.info("[{}] Compensación completada: reserva cancelada y asiento liberado",
                    event.getCorrelationId());

        } catch (Exception e) {
            log.error("Error procesando evento payment-failed desde rawEvent: {}", e.getMessage(), e);
        }
}
}