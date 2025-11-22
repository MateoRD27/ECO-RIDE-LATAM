package org.ecoride.passengerservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PassengerResponseDTO {
    private UUID id;
    private String keycloakSub;
    private String name;
    private String email;
    private Double ratingAvg;
}