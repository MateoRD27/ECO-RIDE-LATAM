package org.ecoride.passengerservice.service;

import org.ecoride.passengerservice.dto.PassengerResponseDTO;

public interface PassengerService {
    PassengerResponseDTO getCurrentPassengerProfile(String keycloakSub);
    PassengerResponseDTO createPassengerIfNotExists(String keycloakSub, String name, String email);
    PassengerResponseDTO getPassengerById(java.util.UUID id);
}