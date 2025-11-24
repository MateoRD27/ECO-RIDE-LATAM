package org.ecoride.passengerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.passengerservice.event.PassengerRatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String PASSENGER_RATED_TOPIC = "passenger-rated";

    /**
     * Emite evento PassengerRated cuando un pasajero califica a otro
     * Este evento puede ser consumido por NotificationService o por métricas
     */
    public void sendPassengerRatedEvent(PassengerRatedEvent event) {
        try {
            log.info("Sending PassengerRated event: ratingId={}, tripId={}, score={}",
                    event.getRatingId(), event.getTripId(), event.getScore());

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                    PASSENGER_RATED_TOPIC,
                    event.getRatingId().toString(),
                    event
            );

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("PassengerRated event sent successfully: ratingId={}, partition={}, offset={}",
                            event.getRatingId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send PassengerRated event: ratingId={}, error={}",
                            event.getRatingId(), ex.getMessage(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error sending PassengerRated event: ratingId={}, error={}",
                    event.getRatingId(), e.getMessage(), e);
            throw new RuntimeException("Failed to send PassengerRated event", e);
        }
    }
}