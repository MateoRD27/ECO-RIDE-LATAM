package org.ecoride.passengerservice.repository;

import org.ecoride.passengerservice.model.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {
    boolean existsByPassengerId(UUID passengerId);
}
