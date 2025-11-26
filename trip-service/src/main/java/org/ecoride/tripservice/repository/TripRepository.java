package org.ecoride.tripservice.repository;

import org.ecoride.tripservice.model.entity.Trip;
import org.ecoride.tripservice.model.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {

    List<Trip> findByDriverIdAndStatus(UUID driverId, TripStatus status);

    @Query("""
        SELECT t FROM Trip t
        WHERE t.status = :status
        AND lower(t.origin) LIKE lower(CONCAT('%', COALESCE(:origin, ''), '%'))
        AND lower(t.destination) LIKE lower(CONCAT('%', COALESCE(:destination, ''), '%'))
        AND t.startTime >= COALESCE(:from, t.startTime)
        AND t.startTime <= COALESCE(:to, t.startTime)
        AND t.seatsAvailable > 0
        ORDER BY t.startTime
        """)
    List<Trip> searchTrips(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("status") TripStatus status
    );


    @Query("SELECT t FROM Trip t WHERE t.driverId = :driverId " +
            "AND t.startTime BETWEEN :start AND :end")
    List<Trip> findDriverTripsInRange(
            @Param("driverId") UUID driverId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}