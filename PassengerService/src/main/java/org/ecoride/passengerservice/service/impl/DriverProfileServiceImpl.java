package org.ecoride.passengerservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecoride.passengerservice.dto.DriverProfileRequestDTO;
import org.ecoride.passengerservice.exception.DriverProfileAlreadyExistsException;
import org.ecoride.passengerservice.exception.ResourceNotFoundException;
import org.ecoride.passengerservice.model.Enum.VerificationStatus;
import org.ecoride.passengerservice.model.entity.DriverProfile;
import org.ecoride.passengerservice.model.entity.Passenger;
import org.ecoride.passengerservice.repository.DriverProfileRepository;
import org.ecoride.passengerservice.repository.PassengerRepository;
import org.ecoride.passengerservice.service.DriverProfileService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j  // ⬅️ Agregar
public class DriverProfileServiceImpl implements DriverProfileService {

    private final DriverProfileRepository driverProfileRepository;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional
    public DriverProfile createProfile(String keycloakSub, DriverProfileRequestDTO request) {
        log.info("Creating driver profile for keycloakSub={}", keycloakSub);

        Passenger passenger = passengerRepository.findByKeycloakSub(keycloakSub)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        if (driverProfileRepository.existsByPassengerId(passenger.getId())) {
            log.warn("Driver profile already exists for passenger: {}", passenger.getId());
            throw new DriverProfileAlreadyExistsException("Driver profile already exists");
        }

        DriverProfile profile = DriverProfile.builder()
                .passenger(passenger)
                .licenseNo(request.getLicenseNo())
                .carPlate(request.getCarPlate())
                .seatsOffered(request.getSeatsOffered() != null ? request.getSeatsOffered() : 1)
                .verificationStatus(VerificationStatus.PENDING)
                .build();

        DriverProfile saved = driverProfileRepository.save(profile);
        log.info("Driver profile created successfully: id={}, passengerId={}", saved.getId(), passenger.getId());  // ⬅️ Agregar
        return saved;
    }
}
