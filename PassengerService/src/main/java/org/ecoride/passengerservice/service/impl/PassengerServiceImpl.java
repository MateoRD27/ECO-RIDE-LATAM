package org.ecoride.passengerservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ecoride.passengerservice.dto.PassengerResponseDTO;
import org.ecoride.passengerservice.exception.ResourceNotFoundException;
import org.ecoride.passengerservice.mapper.PassengerMapper;
import org.ecoride.passengerservice.model.entity.Passenger;
import org.ecoride.passengerservice.repository.PassengerRepository;
import org.ecoride.passengerservice.service.PassengerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    public PassengerResponseDTO getCurrentPassengerProfile(String keycloakSub) {
        Passenger passenger = passengerRepository.findByKeycloakSub(keycloakSub)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        return PassengerMapper.toDto(passenger);
    }

    @Override
    @Transactional
    public PassengerResponseDTO createPassengerIfNotExists(String keycloakSub, String name, String email) {

        Passenger passenger = passengerRepository.findByKeycloakSub(keycloakSub)
                .orElseGet(() -> {
                    Passenger newPassenger = Passenger.builder()
                            .keycloakSub(keycloakSub)
                            .name(name)
                            .email(email)
                            .createdAt(LocalDateTime.now())
                            .ratingAvg(0.0)
                            .build();

                    return passengerRepository.save(newPassenger);
                });

        return PassengerMapper.toDto(passenger);
    }
}
