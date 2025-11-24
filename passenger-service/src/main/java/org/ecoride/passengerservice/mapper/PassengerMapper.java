package org.ecoride.passengerservice.mapper;

import org.ecoride.passengerservice.dto.PassengerResponseDTO;
import org.ecoride.passengerservice.model.entity.Passenger;

public class PassengerMapper {
    public static PassengerResponseDTO toDto(Passenger passenger) {
        return PassengerResponseDTO.builder()
                .id(passenger.getId())
                .keycloakSub(passenger.getKeycloakSub())
                .name(passenger.getName())
                .email(passenger.getEmail())
                .ratingAvg(passenger.getRatingAvg())
                .build();
    }
}