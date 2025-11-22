package org.ecoride.passengerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.passengerservice.event.TripCompletedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Servicio que trackea viajes completados para habilitar ratings
@Service
@RequiredArgsConstructor
@Slf4j
public class TripTrackingService {

    private final Map<UUID, TripCompletedEvent> completedTrips = new ConcurrentHashMap<>();

    // Marca un viaje como completado y elegible para rating
    public void markTripAsRatable(TripCompletedEvent event) {
        completedTrips.put(event.getTripId(), event);
        log.info("Trip {} marked as ratable", event.getTripId());
    }

    //Verifica si un viaje está completado y es elegible para rating
    public boolean isTripRatable(UUID tripId) {
        return completedTrips.containsKey(tripId);
    }

    // Obtiene información de un viaje completado
    public TripCompletedEvent getCompletedTrip(UUID tripId) {
        return completedTrips.get(tripId);
    }
}