package org.ecoride.passengerservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.passengerservice.dto.RatingRequestDTO;
import org.ecoride.passengerservice.event.PassengerRatedEvent;
import org.ecoride.passengerservice.exception.DuplicateRatingException;
import org.ecoride.passengerservice.exception.ResourceNotFoundException;
import org.ecoride.passengerservice.exception.SelfRatingException;
import org.ecoride.passengerservice.exception.TripNotRatableException;
import org.ecoride.passengerservice.model.entity.Passenger;
import org.ecoride.passengerservice.model.entity.Rating;
import org.ecoride.passengerservice.repository.PassengerRepository;
import org.ecoride.passengerservice.repository.RatingRepository;
import org.ecoride.passengerservice.service.KafkaProducerService;
import org.ecoride.passengerservice.service.RatingService;
import org.ecoride.passengerservice.service.TripTrackingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final PassengerRepository passengerRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TripTrackingService tripTrackingService;

    @Override
    @Transactional
    public Rating createRating(String keycloakSub, RatingRequestDTO request) {
        log.info("Creating rating from keycloakSub={} for trip={}", keycloakSub, request.getTripId());

        // Verificar que el viaje esté completado
        if (!tripTrackingService.isTripRatable(request.getTripId())) {
            log.warn("Trip {} is not completed or not eligible for rating", request.getTripId());
            throw new TripNotRatableException("Trip is not completed or not eligible for rating");
        }

        // Obtener el pasajero que califica (from)
        Passenger fromPassenger = passengerRepository.findByKeycloakSub(keycloakSub)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        // Obtener el pasajero a calificar (to)
        Passenger toPassenger = passengerRepository.findById(request.getToPassengerId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver passenger not found"));

        // Verificar que no se califique a sí mismo
        if (fromPassenger.getId().equals(toPassenger.getId())) {
            log.warn("Passenger {} attempted to rate themselves", fromPassenger.getId());
            throw new SelfRatingException("You cannot rate yourself");
        }

        //  Verificar que no exista un rating previo para este viaje
        if (ratingRepository.existsByTripIdAndFromPassengerIdAndToPassengerId(
                request.getTripId(), fromPassenger.getId(), toPassenger.getId())) {
            log.warn("Duplicate rating attempt: tripId={}, from={}, to={}",
                    request.getTripId(), fromPassenger.getId(), toPassenger.getId());
            throw new DuplicateRatingException("Rating already exists for this trip");
        }

        // Crear el rating
        Rating rating = Rating.builder()
                .tripId(request.getTripId())
                .fromPassenger(fromPassenger)
                .toPassenger(toPassenger)
                .score(request.getScore())
                .comment(request.getComment())
                .build();

        Rating savedRating = ratingRepository.save(rating);
        log.info("Rating saved successfully: id={}, score={}", savedRating.getId(), savedRating.getScore());

        // Actualizar el promedio de rating
        updatePassengerRatingAverage(toPassenger.getId());

        // Emitir evento PassengerRated
        PassengerRatedEvent event = PassengerRatedEvent.builder()
                .ratingId(savedRating.getId())
                .tripId(savedRating.getTripId())
                .fromPassengerId(fromPassenger.getId())
                .toPassengerId(toPassenger.getId())
                .score(savedRating.getScore())
                .comment(savedRating.getComment())
                .ratedAt(LocalDateTime.now())
                .build();

        kafkaProducerService.sendPassengerRatedEvent(event);

        return savedRating;
    }

    /**
     * Actualiza el promedio de calificación de un pasajero
     * Calcula el promedio de todas las calificaciones recibidas
     */
    private void updatePassengerRatingAverage(UUID passengerId) {
        List<Rating> ratings = ratingRepository.findAllByToPassengerId(passengerId);

        if (!ratings.isEmpty()) {
            double avg = ratings.stream()
                    .mapToInt(Rating::getScore)
                    .average()
                    .orElse(0.0);

            // Redondear a 2 decimales
            avg = Math.round(avg * 100.0) / 100.0;

            Passenger passenger = passengerRepository.findById(passengerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

            passenger.setRatingAvg(avg);
            passengerRepository.save(passenger);

            log.info("Updated rating average for passenger {}: {} (from {} ratings)",
                    passengerId, avg, ratings.size());
        }
    }
}