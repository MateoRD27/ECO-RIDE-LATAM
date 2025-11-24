
package org.ecoride.passengerservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.passengerservice.event.TripCompletedEvent;
import org.ecoride.passengerservice.service.TripTrackingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripEventListener {

    private final TripTrackingService tripTrackingService;

    //Escucha eventos TripCompleted para habilitar la creación de ratings
     // Este evento se emite cuando un conductor marca un viaje como finalizado
    @KafkaListener(
            topics = "trip-completed",
            groupId = "passenger-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleTripCompleted(
            @Payload TripCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Received TripCompleted event: tripId={}, driverId={}, passengerId={}, partition={}, offset={}",
                    event.getTripId(), event.getDriverId(), event.getPassengerId(), partition, offset);

            // Registrar el viaje como elegible para rating
            tripTrackingService.markTripAsRatable(event);

            log.info("Trip {} marked as ratable for passenger {} and driver {}",
                    event.getTripId(), event.getPassengerId(), event.getDriverId());

            // Acknowledge manual para garantizar procesamiento
            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Error processing TripCompleted event: tripId={}, error={}",
                    event.getTripId(), e.getMessage(), e);
            // No hacer acknowledge para reintento
        }
    }
}