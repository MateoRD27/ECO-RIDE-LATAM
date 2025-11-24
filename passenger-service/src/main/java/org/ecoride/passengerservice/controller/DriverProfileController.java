package org.ecoride.passengerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecoride.passengerservice.dto.DriverProfileRequestDTO;
import org.ecoride.passengerservice.model.entity.DriverProfile;
import org.ecoride.passengerservice.service.DriverProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Profile", description = "Driver profile management")
@SecurityRequirement(name = "bearer-jwt")
public class DriverProfileController {

    private final DriverProfileService driverProfileService;

    @PostMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Create driver profile", description = "Create driver profile for authenticated passenger")
    public ResponseEntity<DriverProfile> createProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DriverProfileRequestDTO request) {

        String keycloakSub = jwt.getSubject();
        DriverProfile profile = driverProfileService.createProfile(keycloakSub, request);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }
}
