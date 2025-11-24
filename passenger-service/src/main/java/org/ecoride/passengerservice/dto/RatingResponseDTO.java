package org.ecoride.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDTO {
    private UUID id;
    private UUID tripId;
    private UUID fromPassengerId;
    private UUID toPassengerId;
    private Integer score;
    private String comment;
}