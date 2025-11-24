package org.ecoride.passengerservice.repository;

import org.ecoride.passengerservice.model.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    Optional<Passenger> findByKeycloakSub(String keycloakSub);
}
