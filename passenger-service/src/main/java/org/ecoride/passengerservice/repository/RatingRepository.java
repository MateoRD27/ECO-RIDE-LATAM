package org.ecoride.passengerservice.repository;

import org.ecoride.passengerservice.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    // Verifica si ya existe una calificación para un mismo viaje
    boolean existsByTripIdAndFromPassengerIdAndToPassengerId(UUID tripId, UUID fromId, UUID toId);
    List<Rating> findAllByToPassengerId(UUID toPassengerId);

}
