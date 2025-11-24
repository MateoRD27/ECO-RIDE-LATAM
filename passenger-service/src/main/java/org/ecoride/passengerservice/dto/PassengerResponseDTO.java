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
public class PassengerResponseDTO {
    private UUID id;
    private String keycloakSub;
    private String name;
    private String email;
    private Double ratingAvg;
}