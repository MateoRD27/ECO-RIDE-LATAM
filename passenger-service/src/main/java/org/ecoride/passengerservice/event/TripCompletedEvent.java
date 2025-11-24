package org.ecoride.passengerservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

//Evento que se recibe
public class TripCompletedEvent {
    private UUID tripId;
    private UUID driverId;
    private UUID passengerId;
    private LocalDateTime completedAt;
}