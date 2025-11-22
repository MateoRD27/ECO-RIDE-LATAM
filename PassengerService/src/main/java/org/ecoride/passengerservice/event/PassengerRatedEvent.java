package org.ecoride.passengerservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Evento que se emite

public class PassengerRatedEvent {
    private UUID ratingId;
    private UUID tripId;
    private UUID fromPassengerId;
    private UUID toPassengerId;
    private Integer score;
    private String comment;
    private LocalDateTime ratedAt;
}