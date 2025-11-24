package org.ecoride.passengerservice.service;

import org.ecoride.passengerservice.dto.DriverProfileRequestDTO;
import org.ecoride.passengerservice.model.entity.DriverProfile;

public interface DriverProfileService {
    DriverProfile createProfile(String keycloakSub, DriverProfileRequestDTO request);
}