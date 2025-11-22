package org.ecoride.passengerservice.service;

import org.ecoride.passengerservice.dto.RatingRequestDTO;
import org.ecoride.passengerservice.model.entity.Rating;

public interface RatingService {
    Rating createRating(String keycloakSub, RatingRequestDTO request);
}