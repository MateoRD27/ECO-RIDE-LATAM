package org.ecoride.passengerservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RatingRequestDTO {
    private UUID tripId;
    private UUID toPassengerId;
    private Integer score;
    private String comment;
}
