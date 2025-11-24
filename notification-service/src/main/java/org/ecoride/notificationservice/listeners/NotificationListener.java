package org.ecoride.notificationservice.listeners;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.notificationservice.events.ReservationEvents;
import org.ecoride.notificationservice.service.NotificationDomainService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    private final NotificationDomainService notificationService;

    /**
     * CASO 1: Pago Fallido (Viene de PaymentService)
     */
    @KafkaListener(topics = "payment-failed", groupId = "notification-service-group")
    public void handlePaymentFailed(ReservationEvents.PaymentFailed event) {
        log.info("[{}] Procesando PaymentFailed para Pasajero: {}", event.getCorrelationId(), event.getPassengerId());

        String subject = "Problema con el pago de tu reserva";
        String message = String.format(
                "Hola, intentamos procesar el pago para tu reserva %s pero falló.\n\nRazón: %s\n\nPor favor intenta nuevamente.",
                event.getReservationId(),
                event.getReason()
        );

        notificationService.sendNotification(event.getPassengerId(), subject, message);
    }

    /**
     * CASO 2: Reserva Confirmada (Viene de TripService tras pago exitoso)
     */
    @KafkaListener(topics = "reservation-confirmed", groupId = "notification-service-group")
    public void handleReservationConfirmed(ReservationEvents.ReservationConfirmed event) {
        log.info("[{}] Procesando ReservationConfirmed para Pasajero: {}", event.getCorrelationId(), event.getPassengerId());

        String subject = "¡Reserva Confirmada en Eco-Ride!";
        String message = String.format(
                "¡Buenas noticias! Tu reserva %s ha sido confirmada exitosamente.\nPrepárate para tu viaje.",
                event.getReservationId()
        );

        notificationService.sendNotification(event.getPassengerId(), subject, message);
    }

    /**
     * CASO 3: Reserva Cancelada (Viene de TripService por compensación o manual)
     */
    @KafkaListener(topics = "reservation-cancelled", groupId = "notification-service-group")
    public void handleReservationCancelled(ReservationEvents.ReservationCancelled event) {
        log.info("[{}] Procesando ReservationCancelled para Pasajero: {}", event.getCorrelationId(), event.getPassengerId());

        String subject = "Tu reserva ha sido cancelada";
        String message = String.format(
                "La reserva %s ha sido cancelada.\n\nMotivo: %s\n\nEsperamos verte pronto en otro viaje.",
                event.getReservationId(),
                event.getReason()
        );

        notificationService.sendNotification(event.getPassengerId(), subject, message);
    }
}
