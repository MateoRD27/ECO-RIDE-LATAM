package org.ecoride.passengerservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecoride.passengerservice.dto.RatingRequestDTO;
import org.ecoride.passengerservice.exception.ResourceNotFoundException;
import org.ecoride.passengerservice.exception.SelfRatingException;
import org.ecoride.passengerservice.model.entity.Passenger;
import org.ecoride.passengerservice.model.entity.Rating;
import org.ecoride.passengerservice.repository.PassengerRepository;
import org.ecoride.passengerservice.repository.RatingRepository;
import org.ecoride.passengerservice.service.RatingService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final PassengerRepository passengerRepository;

    @Override
    public Rating createRating(String keycloakSub, RatingRequestDTO request) {

        Passenger fromPassenger = passengerRepository.findByKeycloakSub(keycloakSub)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        Passenger toPassenger = passengerRepository.findById(request.getToPassengerId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver passenger not found"));

        if (fromPassenger.getId().equals(toPassenger.getId())) {
            throw new SelfRatingException("You cannot rate yourself");
        }

        Rating rating = Rating.builder()
                .tripId(request.getTripId())
                .fromPassenger(fromPassenger)
                .toPassenger(toPassenger)
                .score(request.getScore())
                .comment(request.getComment())
                .build();

        return ratingRepository.save(rating);
    }

}
